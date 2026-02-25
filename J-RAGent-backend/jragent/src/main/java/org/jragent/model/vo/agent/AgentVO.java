package org.jragent.model.vo.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.jragent.model.dto.agent.AgentDTO;

import java.util.List;

@Data
@Builder
@Schema(name = "AgentVO", description = "智能体展示对象")
public class AgentVO {
    private String id;

    private String name;

    private String description;

    private String systemPrompt;

    private AgentDTO.ModelType model;

    private List<String> allowedTools;

//    private List<String> allowedKbs;

    private AgentDTO.ChatOptions chatOptions;
}

