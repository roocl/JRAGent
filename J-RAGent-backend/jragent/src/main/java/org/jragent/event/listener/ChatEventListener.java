package org.jragent.event.listener;

import org.jragent.agent.ChatAgent;
import org.jragent.agent.ChatAgentFactory;
import org.jragent.event.ChatEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatEventListener {

    private final ChatAgentFactory chatAgentFactory;

    @Async
    @EventListener
    public void handle(ChatEvent event) {
        // 创建一个 Agent 实例处理聊天事件
        ChatAgent chatAgent = chatAgentFactory.createChatAgent(event.getAgentId(), event.getSessionId());
        chatAgent.run();
    }
}
