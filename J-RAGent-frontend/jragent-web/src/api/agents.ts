import { http, unwrap } from "../lib/http";
import type {
  CreateAgentPayload,
  CreateAgentResponse,
  GetAgentResponse,
  GetAgentsResponse,
  UpdateAgentPayload,
} from "../types/agent";

export async function listAgents() {
  return unwrap<GetAgentsResponse>(http.get("/api/agents"));
}

export async function getAgent(agentId: string) {
  return unwrap<GetAgentResponse>(http.get(`/api/agents/${agentId}`));
}

export async function createAgent(payload: CreateAgentPayload) {
  return unwrap<CreateAgentResponse>(http.post("/api/agents", payload));
}

export async function updateAgent(agentId: string, payload: UpdateAgentPayload) {
  return unwrap<void>(http.patch(`/api/agents/${agentId}`, payload));
}

export async function deleteAgent(agentId: string) {
  return unwrap<void>(http.delete(`/api/agents/${agentId}`));
}
