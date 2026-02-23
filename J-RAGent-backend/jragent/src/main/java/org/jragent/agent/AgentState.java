package org.jragent.agent;

public enum AgentState {
    // 空闲
    IDLE,
    // 计划中
    PLANNING,
    // 思考中
    THINKING,
    // 执行中
    EXECUTING,
    // 结束
    FINISHED,
    // 错误
    ERROR
}
