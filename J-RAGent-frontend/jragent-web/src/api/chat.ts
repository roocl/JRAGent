import { http, unwrap } from "../lib/http";
import type { CreateAgentResponse, CreateMessageResponse, CreateSessionResponse, Role } from "../types/chat";
// 配置agent -> 生成会话 -> 开始聊天
export async function createAgent() {
    return unwrap<CreateAgentResponse>(
        http.post("/api/agents", {
            name: "默认助手",
            description: "前端首屏测试",
            systemPrompt: "你是一个简洁的中文助手",
            model: "deepseek-chat",
            allowedTools: ["WeatherTestTool", "DateTestTool", "CityTestTool"],
            chatOptions: { temperature: 0.7, topP: 1.0, messageLength: 10 },
        }),
    );
}

export async function createSession(agentId: string) {
    return unwrap<CreateSessionResponse>(http.post("/api/chat-sessions", { agentId, title: "新会话" }));
}

export async function sendMessage(input: {
    agentId: string;
    sessionId: string;
    role: Role;
    content: string;
}) {
    return unwrap<CreateMessageResponse>(http.post("/api/chat-messages", input));
}
