import { http, unwrap } from "../lib/http";
import type {
  CreateChatMessagePayload,
  CreateChatMessageResponse,
  GetChatMessagesResponse,
  UpdateChatMessagePayload,
} from "../types/message";

export async function listChatMessagesBySession(sessionId: string) {
  return unwrap<GetChatMessagesResponse>(http.get(`/api/chat-messages/session/${sessionId}`));
}

export async function createChatMessage(payload: CreateChatMessagePayload) {
  return unwrap<CreateChatMessageResponse>(http.post("/api/chat-messages", payload));
}

export async function updateChatMessage(chatMessageId: string, payload: UpdateChatMessagePayload) {
  return unwrap<void>(http.patch(`/api/chat-messages/${chatMessageId}`, payload));
}

export async function deleteChatMessage(chatMessageId: string) {
  return unwrap<void>(http.delete(`/api/chat-messages/${chatMessageId}`));
}
