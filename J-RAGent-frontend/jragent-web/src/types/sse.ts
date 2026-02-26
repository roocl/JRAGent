import type { ChatMessage } from "./message";

export type SseType =
  | "AI_GENERATED_CONTENT"
  | "AI_PLANNING"
  | "AI_THINKING"
  | "AI_EXECUTING"
  | "AI_DONE";

export type SseMessage = {
  type: SseType;
  payload?: {
    message?: ChatMessage;
    statusText?: string;
    done?: boolean;
  };
  metadata?: {
    chatMessageId?: string;
  };
};
