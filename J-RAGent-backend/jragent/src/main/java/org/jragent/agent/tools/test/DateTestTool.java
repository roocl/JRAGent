package org.jragent.agent.tools.test;

import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTestTool implements Tool {
    @Override
    public String getName() {
        return "DateTestTool";
    }

    @Override
    public String getDescription() {
        return "获取当前的日期";
    }

    @Override
    public ToolType getType() {
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "Date", description = "获取当前的日期")
    public String getDate() {
        return "2025-09-01";
    }
}
