// 泛型接口
export type ApiResponse<T> = {
    code: number;
    message: string;
    data: T;
};
// 角色定义
export type Role = "user" | "assistant" | "system" | "tool";
// 创建类接口响应
export type CreateAgentResponse = { agentId: string };
export type CreateSessionResponse = { chatSessionId: string };
export type CreateMessageResponse = { chatMessageId: string };
// 聊天消息结构
export type ChatMessage = {
    id?: string;
    sessionId: string;
    role: Role;
    content: string;
};
// Sse消息结构
export type SseMessage = {
    type: "AI_GENERATED_CONTENT" | "AI_PLANNING" | "AI_THINKING" | "AI_EXECUTING" | "AI_DONE";
    payload?: {
        message?: {
            id?: string;
            sessionId: string;
            role: Role;
            content: string;
        };
        statusText?: string;
        done?: boolean;
    };
    metadata?: { chatMessageId?: string };
};
