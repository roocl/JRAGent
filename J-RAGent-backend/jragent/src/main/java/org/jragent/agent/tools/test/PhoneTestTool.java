package org.jragent.agent.tools.test;

import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.springframework.stereotype.Component;

@Component
public class PhoneTestTool implements Tool {
    @Override
    public String getName() {
        return "PhoneTestTool";
    }

    @Override
    public String getDescription() {
        return "用来测试可选工具";
    }

    @Override
    public ToolType getType() {
        return ToolType.OPTIONAL;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "Phone", description = "手机列表")
    public String getDate() {
        return "苹果 | 安卓";
    }
}
