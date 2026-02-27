package org.jragent.service.impl;

import lombok.Data;
import org.jragent.mapper.ChunkBgeM3Mapper;
import org.jragent.model.entity.ChunkBgeM3;
import org.jragent.service.RagService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class RagServiceImpl implements RagService {

    private final WebClient webClient;

    private final ChunkBgeM3Mapper chunkBgeM3Mapper;

    public RagServiceImpl(WebClient.Builder webClientBuilder, ChunkBgeM3Mapper chunkBgeM3Mapper) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:11434").build();
        this.chunkBgeM3Mapper = chunkBgeM3Mapper;
    }

    @Data
    private static class EmbeddingResponse {
        private float[] embedding;
    }

    private float[] doEmbed(String text) {
        EmbeddingResponse response = webClient.post()
                .uri("/api/embeddings")
                .bodyValue(Map.of(
                        "model", "bge-m3",
                        "prompt", text
                ))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)  // 反序列化为response对象
                .block(); // 同步等待
        Assert.notNull(response, "EmbeddingResponse must not be null");
        return response.getEmbedding();
    }

    @Override
    public float[] embed(String text) {
        return doEmbed(text);
    }

    @Override
    public List<String> similaritySearch(String kbId, String title) {
        // 文本转向量
        String queryEmbedding = toPgVector(doEmbed(title));
        // 在知识库查找最相似的前几条
        List<ChunkBgeM3> chunkBgeM3s = chunkBgeM3Mapper.similaritySearch(kbId, queryEmbedding, 3);
        // 提取内容并返回
        return chunkBgeM3s.stream().map(ChunkBgeM3::getContent).toList();
    }

    /* 手动拼接出能被pgvector识别的向量格式 */
    private String toPgVector(float[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i != vector.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
