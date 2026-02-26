export type ChatSession = {
  id: string;
  agentId: string;
  title: string;
};

export type GetChatSessionResponse = {
  chatSession: ChatSession;
};

export type GetChatSessionsResponse = {
  chatSessions: ChatSession[];
};

export type CreateChatSessionPayload = {
  agentId: string;
  title: string;
};

export type CreateChatSessionResponse = {
  chatSessionId: string;
};

export type UpdateChatSessionPayload = {
  title?: string;
};
