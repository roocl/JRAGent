package org.jragent.service;

import org.jragent.agent.tools.Tool;

import java.util.List;

public interface ToolService {
    List<Tool> getAllTools();

    List<Tool> getOptionalTools();

    List<Tool> getFixedTools();
}
