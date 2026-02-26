import { http, unwrap } from "../lib/http";
import type {
  CreateChatSessionPayload,
  CreateChatSessionResponse,
  GetChatSessionResponse,
  GetChatSessionsResponse,
  UpdateChatSessionPayload,
} from "../types/session";

export async function listChatSessions() {
  return unwrap<GetChatSessionsResponse>(http.get("/api/chat-sessions"));
}

export async function listChatSessionsByAgent(agentId: string) {
  return unwrap<GetChatSessionsResponse>(http.get(`/api/chat-sessions/agent/${agentId}`));
}

export async function getChatSession(chatSessionId: string) {
  return unwrap<GetChatSessionResponse>(http.get(`/api/chat-sessions/${chatSessionId}`));
}

export async function createChatSession(payload: CreateChatSessionPayload) {
  return unwrap<CreateChatSessionResponse>(http.post("/api/chat-sessions", payload));
}

export async function updateChatSession(chatSessionId: string, payload: UpdateChatSessionPayload) {
  return unwrap<void>(http.patch(`/api/chat-sessions/${chatSessionId}`, payload));
}

export async function deleteChatSession(chatSessionId: string) {
  return unwrap<void>(http.delete(`/api/chat-sessions/${chatSessionId}`));
}
