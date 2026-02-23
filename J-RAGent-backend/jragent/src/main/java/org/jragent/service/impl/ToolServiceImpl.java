package org.jragent.service.impl;

import lombok.AllArgsConstructor;
import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.jragent.service.ToolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ToolServiceImpl implements ToolService {

    private final List<Tool> tools;

    @Override
    public List<Tool> getAllTools() {
        return tools;
    }

    @Override
    public List<Tool> getOptionalTools() {
        return getToolsByType(ToolType.OPTIONAL);
    }

    @Override
    public List<Tool> getFixedTools() {
        return getToolsByType(ToolType.FIXED);
    }

    private List<Tool> getToolsByType(ToolType type) {
        return tools.stream()
                .filter(tool -> tool.getType().equals(type))
                .toList();
    }
}
