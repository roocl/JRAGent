package org.jragent.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jragent.agent.tools.Tool;
import org.jragent.converter.KnowledgeBaseConverter;
import org.jragent.mapper.KnowledgeBaseMapper;
import org.jragent.model.dto.knowledgeBase.KnowledgeBaseDTO;
import org.jragent.model.entity.KnowledgeBase;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import lombok.extern.slf4j.Slf4j;
import org.jragent.config.ChatClientRegistry;
import org.jragent.converter.AgentConverter;
import org.jragent.converter.ChatMessageConverter;
import org.jragent.mapper.AgentMapper;
import org.jragent.model.dto.agent.AgentDTO;
import org.jragent.model.dto.chatMessage.ChatMessageDTO;
import org.jragent.service.ChatMessageService;
import org.jragent.service.SseService;
import org.jragent.service.ToolService;
import org.jragent.model.entity.Agent;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChatAgentFactory {

    private final ChatClientRegistry chatClientRegistry;

    private final SseService sseService;

    private final AgentMapper agentMapper;

    private final AgentConverter agentConverter;

    private final KnowledgeBaseMapper knowledgeBaseMapper;

    private final KnowledgeBaseConverter knowledgeBaseConverter;

    private final ToolService toolService;

    private final ChatMessageService chatMessageService;

    private final ChatMessageConverter chatMessageConverter;

    public ChatAgentFactory(
            ChatClientRegistry chatClientRegistry,
            SseService sseService,
            AgentMapper agentMapper,
            AgentConverter agentConverter,
            KnowledgeBaseMapper knowledgeBaseMapper,
            KnowledgeBaseConverter knowledgeBaseConverter,
            ToolService toolService,
            ChatMessageService chatMessageService,
            ChatMessageConverter chatMessageConverter) {
        this.chatClientRegistry = chatClientRegistry;
        this.sseService = sseService;
        this.agentMapper = agentMapper;
        this.agentConverter = agentConverter;
        this.knowledgeBaseMapper = knowledgeBaseMapper;
        this.knowledgeBaseConverter = knowledgeBaseConverter;
        this.toolService = toolService;
        this.chatMessageService = chatMessageService;
        this.chatMessageConverter = chatMessageConverter;
    }

    private Agent loadAgent(String agentId) {
        return agentMapper.selectById(agentId);
    }

    private List<Message> loadMemories(String chatSessionId, AgentDTO agentConfig) {
        int messageLength = agentConfig.getChatOptions().getMessageLength();
        List<ChatMessageDTO> chatMessages = chatMessageService.getChatMessagesBySessionIdRecently(chatSessionId,
                messageLength);
        List<Message> memories = new ArrayList<>();
        for (ChatMessageDTO chatMessageDTO : chatMessages) {
            switch (chatMessageDTO.getRole()) {
                case SYSTEM:
                    if (!StringUtils.hasLength(chatMessageDTO.getContent())) {
                        continue;
                    }
                    memories.add(0, new SystemMessage(chatMessageDTO.getContent()));
                    break;
                case USER:
                    if (!StringUtils.hasLength(chatMessageDTO.getContent()))
                        continue;
                    memories.add(new UserMessage(chatMessageDTO.getContent()));
                    break;
                case ASSISTANT:
                    ChatMessageDTO.MetaData assistantMeta = chatMessageDTO.getMetadata();
                    memories.add(AssistantMessage.builder()
                            .content(chatMessageDTO.getContent())
                            .toolCalls(assistantMeta != null ? assistantMeta.getToolCalls() : List.of())
                            .build());
                    break;
                case TOOL:
                    ChatMessageDTO.MetaData toolMeta = chatMessageDTO.getMetadata();
                    if (toolMeta == null || toolMeta.getToolResponse() == null) {
                        log.warn("TOOL 消息缺少 metadata/toolResponse, 跳过: id={}", chatMessageDTO.getId());
                        continue;
                    }
                    memories.add(ToolResponseMessage.builder()
                            .responses(List.of(toolMeta.getToolResponse()))
                            .build());
                    break;
                default:
                    log.error("不支持的Message类型: {}, content = {}",
                            chatMessageDTO.getRole().getRole(),
                            chatMessageDTO.getContent());
                    throw new IllegalStateException("不支持的Message类型");
            }
        }
        return memories;
    }

    private AgentDTO toAgentConfig(Agent agent) {
        try {
            return agentConverter.toDTO(agent);
        } catch (Exception e) {
            throw new IllegalStateException("解析Agent配置失败", e);
        }
    }

    private List<Tool> resolveRuntimeTools(AgentDTO agentConfig) {
        // 固定工具
        List<Tool> runtimeTools = new ArrayList<>(toolService.getFixedTools());

        // 可选工具
        List<String> allowedToolNames = agentConfig.getAllowedTools();
        if (allowedToolNames == null || allowedToolNames.isEmpty()) {
            return runtimeTools;
        }

        Map<String, Tool> optionalToolMap = toolService.getOptionalTools()
                .stream()
                .collect(Collectors.toMap(Tool::getName, Function.identity()));

        for (String toolName : allowedToolNames) {
            Tool tool = optionalToolMap.get(toolName);
            if (tool != null) {
                runtimeTools.add(tool);
            }
        }
        return runtimeTools;
    }

    private List<KnowledgeBaseDTO> resolveRuntimeKnowledgeBases(AgentDTO agentConfig) {
        List<String> allowedKbIds = agentConfig.getAllowedKbs();
        if (allowedKbIds == null || allowedKbIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.selectByIdBatch(allowedKbIds);
        if (knowledgeBases.isEmpty()) {
            return Collections.emptyList();
        }
        List<KnowledgeBaseDTO> kbDTOs = new ArrayList<>();
        try {
            for (KnowledgeBase knowledgeBase : knowledgeBases) {
                KnowledgeBaseDTO kbDTO = knowledgeBaseConverter.toDTO(knowledgeBase);
                kbDTOs.add(kbDTO);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("解析知识库配置失败", e);
        }
        return kbDTOs;
    }

    /* 将Tool对象转换成可被Spring AI执行的ToolCallBack */
    private List<ToolCallback> buildToolCallbacks(List<Tool> runtimeTools) {
        List<ToolCallback> callbacks = new ArrayList<>();
        for (Tool tool : runtimeTools) {
            Object target = resolveToolTarget(tool);
            // 扫描对象，提取工具方法
            ToolCallback[] toolCallbacks = MethodToolCallbackProvider.builder()
                    .toolObjects(target)
                    .build()
                    .getToolCallbacks();
            // 存入当前Tool对象的所有工具方法
            callbacks.addAll(Arrays.asList(toolCallbacks));
        }
        return callbacks;
    }

    private Object resolveToolTarget(Tool tool) {
        // 后续优化拓展？
        return tool;
    }

    private ChatAgent buildChatAgentRuntime(
            Agent agent,
            AgentDTO agentConfig,
            List<Message> memories,
            List<ToolCallback> toolCallbacks,
            List<KnowledgeBaseDTO> knowledgeBases,
            String chatSessionId) {
        ChatClient chatClient = chatClientRegistry.get(agent.getModel());

        if (Objects.isNull((chatClient))) {
            throw new IllegalStateException("未找到对应的 ChatClient: " + agent.getModel());
        }
        return new ChatAgent(
                agent.getId(),
                agent.getName(),
                agent.getDescription(),
                agent.getSystemPrompt(),
                chatClient,
                agentConfig.getChatOptions().getMessageLength(),
                memories,
                toolCallbacks,
                knowledgeBases,
                chatSessionId,
                sseService,
                chatMessageService,
                chatMessageConverter);
    }

    public ChatAgent createChatAgent(String agentId, String chatSessionId) {
        Agent agent = loadAgent(agentId);
        AgentDTO agentConfig = toAgentConfig(agent);
        List<Message> memories = loadMemories(chatSessionId, agentConfig);

        List<Tool> runtimeTools = resolveRuntimeTools(agentConfig);
        List<KnowledgeBaseDTO> knowledgeBases = resolveRuntimeKnowledgeBases(agentConfig);

        List<ToolCallback> toolCallbacks = buildToolCallbacks(runtimeTools);

        return buildChatAgentRuntime(agent, agentConfig, memories, toolCallbacks, knowledgeBases, chatSessionId);
    }
}
