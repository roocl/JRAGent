package org.jragent.agent.tools;

import org.springframework.stereotype.Component;

@Component
public class TerminateTool implements Tool {

    @Override
    public String getName() {
        return "terminate";
    }

    @Override
    public String getDescription() {
        return "跳出Agent Loop";
    }

    @Override
    public ToolType getType() {
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "terminate", description = "当任务完成且不再需要任何工具调用时执行该工具调用")
    public void terminate() {}
}
