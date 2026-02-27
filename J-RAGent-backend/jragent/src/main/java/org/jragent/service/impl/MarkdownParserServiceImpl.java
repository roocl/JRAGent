package org.jragent.service.impl;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ext.tables.TableBlock;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Block;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import lombok.extern.slf4j.Slf4j;
import org.jragent.service.MarkdownParserService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MarkdownParserServiceImpl implements MarkdownParserService {

    private final Parser parser;

    private String originalMarkdownContent;

    public MarkdownParserServiceImpl() {
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
    }


    @Override
    public List<MarkdownSection> parseMarkdown(InputStream inputStream) {
        try {
            // 读取文件内容
            originalMarkdownContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // 解析markdown
            Document document = parser.parse(originalMarkdownContent);

            // 提取标题和内容
            List<MarkdownSection> sections = new ArrayList<>();
            extractSections(document, sections);

            log.info("解析 Markdown 完成，共提取 {} 个章节", sections.size());
            return sections;
        } catch (Exception e) {
            log.error("解析 Markdown 失败", e);
            throw new RuntimeException("解析 Markdown 失败: " + e.getMessage(), e);
        }
    }

    /*提取标题和内容*/
    private void extractSections(Document document, List<MarkdownSection> sections) {
        // 收集文档顶层节点
        List<Node> topLevelNodes = new ArrayList<>();
        Node child = document.getFirstChild();
        while (child != null) {
            topLevelNodes.add(child);
            child = child.getNext();
        }

        // 遍历顶层节点，找到所有标题
        for (int i = 0; i < topLevelNodes.size(); i++) {
            Node node = topLevelNodes.get(i);

            if (node instanceof Heading heading) {
                String title = extractHeadingText(heading);

                if (title.trim().isEmpty()) {
                    continue;
                }

                // 收集当前标题与下一任意级别标题之间的所有内容
                StringBuilder contentBuilder = new StringBuilder();
                for (int j = i + 1; j < topLevelNodes.size(); j++) {
                    Node nextNode = topLevelNodes.get(j);

                    // 标题挨着标题
                    if (nextNode instanceof Heading) {
                        break;
                    }

                    String NodeContent = extractNodeContent(nextNode);
                    if (NodeContent != null && !NodeContent.trim().isEmpty()) {
                        if (!contentBuilder.isEmpty()) {
                            contentBuilder.append("\n");
                        }
                        contentBuilder.append(NodeContent);
                    }
                }
                String content = contentBuilder.toString().trim();
                if (!content.isEmpty()) {
                    sections.add(new MarkdownSection(title, content));
                }
            }
        }
    }

    /*提取标题文本*/
    private String extractHeadingText(Heading heading) {
        StringBuilder text = new StringBuilder();
        // 收集标题节点
        Node child = heading.getFirstChild();
        while (child != null) {
            String childText = extractPlainText(child);
            if (childText != null && !childText.trim().isEmpty()) {
                // 分隔节点，避免文字合到一起
                if (!text.isEmpty()) {
                    text.append(" ");
                }
                text.append(childText);
            }
            child = child.getNext();
        }
        return text.toString().trim();
    }

    /*提取节点内容*/
    private String extractNodeContent(Node node) {
        // 表格节点保留原始格式，其他节点提取文本内容
        return node == null ? null : (node instanceof TableBlock ? extractTableMarkdown(node) : extractPlainText(node));
    }

    /*提取表格的原始markdown格式*/
    private String extractTableMarkdown(Node tableNode) {
        // 如果没拿到原始markdown全文，就用纯文本提取
        if (originalMarkdownContent == null) {
            return extractPlainText(tableNode);
        }

        try {
            BasedSequence chars = tableNode.getChars();
            if (!chars.isEmpty()) {
                int startOffset = chars.getStartOffset();
                int endOffset = chars.getEndOffset();

                // 防止越界
                if (startOffset >= 0 && endOffset <= originalMarkdownContent.length() && startOffset < endOffset) {
                    String tableMarkdown = originalMarkdownContent.substring(startOffset, endOffset);
                    return tableMarkdown.trim();
                }
            }

            // 如果无法从原始内容提取，尝试从节点本身提取
            return extractPlainText(tableNode);
        } catch (Exception e) {
            log.warn("提取表格 Markdown 失败，使用文本提取: {}", e.getMessage());
            return extractPlainText(tableNode);
        }
    }

    /*提取节点的纯文本内容*/
    private String extractPlainText(Node node) {
        if (node == null) {
            return null;
        }

        StringBuilder text = new StringBuilder();
        extractTextRecursive(node, text);
        return !text.isEmpty() ? text.toString().trim() : null;
    }

    private void extractTextRecursive(Node node, StringBuilder text) {
        // 节点为空或为标题节点（已经在extractSections处理过了）
        if (node == null || node instanceof Heading) {
            return;
        }

        // 对于有子节点的节点，开始dfs递归处理
        Node child = node.getFirstChild();
        if (child != null) {
            boolean isFirstChild = true;
            while (child != null) {
                if (!isFirstChild && !text.isEmpty()) {
                    // 检查是否需要换行
                    if (child instanceof Block) {
                        if (!text.toString().endsWith("\n")) {
                            text.append("\n");
                        }
                    } else {
                        // 防止同级文字合在一起
                        text.append(" ");
                    }
                }
                extractTextRecursive(child, text);
                child = child.getNext();
                isFirstChild = false;
            }
        } else {
            // 对于叶子节点，直接提取文本
            try {
                BasedSequence chars = node.getChars();
                if (!chars.isEmpty()) {
                    String nodeText = chars.toString().trim();
                    if (!nodeText.isEmpty()) {
                        // 非首段文本时，写入前检查分隔符，防止文字合在一起
                        if (!text.isEmpty()) {
                            String lastChar = text.substring(text.length() - 1);
                            if (!lastChar.equals("\n") && !lastChar.equals(" ")) {
                                text.append(" ");
                            }
                        }
                        text.append(nodeText);
                    }
                }
            } catch (Exception e) {
                log.warn("提取叶子节点文本时发生错误: {}", e.getMessage());
            }
        }
    }
}
