import { createAgent as createAgentApi } from "./agents";
import { createChatMessage } from "./messages";
import { createChatSession } from "./sessions";
import type { Role } from "../types/message";

export async function createAgent() {
  return createAgentApi({
    name: "默认助手",
    description: "前端首屏测试",
    systemPrompt: "你是一个简洁的中文助手",
    model: "deepseek-chat",
    allowedTools: ["WeatherTestTool", "DateTestTool", "CityTestTool"],
    chatOptions: { temperature: 0.7, topP: 1.0, messageLength: 10 },
  });
}

export async function createSession(agentId: string) {
  return createChatSession({ agentId, title: "新会话" });
}

export async function sendMessage(input: {
  agentId: string;
  sessionId: string;
  role: Role;
  content: string;
}) {
  return createChatMessage(input);
}
