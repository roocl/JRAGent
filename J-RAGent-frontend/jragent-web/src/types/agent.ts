export type AgentModel = "deepseek-chat" | "glm-4.7" | string;

export type AgentChatOptions = {
  temperature: number;
  topP: number;
  messageLength: number;
};

export type Agent = {
  id: string;
  name: string;
  description: string;
  systemPrompt: string;
  model: AgentModel;
  allowedTools: string[];
  chatOptions: AgentChatOptions;
};

export type GetAgentResponse = {
  agent: Agent;
};

export type GetAgentsResponse = {
  agents: Agent[];
};

export type CreateAgentPayload = {
  name: string;
  description: string;
  systemPrompt: string;
  model: AgentModel;
  allowedTools: string[];
  chatOptions: AgentChatOptions;
};

export type CreateAgentResponse = {
  agentId: string;
};

export type UpdateAgentPayload = Partial<CreateAgentPayload>;
