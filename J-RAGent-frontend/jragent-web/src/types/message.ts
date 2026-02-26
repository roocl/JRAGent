export type Role = "user" | "assistant" | "system" | "tool";

export type ChatMessageMetadata = {
  toolResponse?: unknown;
  toolCalls?: unknown[];
};

export type ChatMessage = {
  id?: string;
  sessionId: string;
  role: Role;
  content: string;
  metadata?: ChatMessageMetadata | null;
};

export type GetChatMessagesResponse = {
  chatMessages: ChatMessage[];
};

export type CreateChatMessagePayload = {
  agentId: string;
  sessionId: string;
  role: Role;
  content: string;
  metadata?: ChatMessageMetadata;
};

export type CreateChatMessageResponse = {
  chatMessageId: string;
};

export type UpdateChatMessagePayload = {
  content?: string;
  metadata?: ChatMessageMetadata;
};
