import { useEffect, useMemo, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createAgent, createSession, sendMessage } from "../api/chat";
import type { ChatMessage } from "../types/message";
import type { SseMessage } from "../types/sse";

export default function ChatPage() {
    const navigate = useNavigate();
    const { agentId: routeAgentId, sessionId: routeSessionId } = useParams();

    const [agentId, setAgentId] = useState("");
    const [sessionId, setSessionId] = useState("");
    const [sseState, setSseState] = useState<"idle" | "connected" | "error">("idle");
    const [text, setText] = useState("");
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const esRef = useRef<EventSource | null>(null);

    const ready = useMemo(() => !!agentId && !!sessionId, [agentId, sessionId]);

    function connectSse(currentSessionId: string) {
        esRef.current?.close();

        const es = new EventSource(`/sse/connect/${currentSessionId}`);
        esRef.current = es;

        es.addEventListener("init", () => setSseState("connected"));
        es.addEventListener("message", (evt) => {
            try {
                const data = JSON.parse((evt as MessageEvent).data) as SseMessage;
                const m = data.payload?.message;
                if (m) {
                    setMessages((prev) => [...prev, { id: m.id, sessionId: m.sessionId, role: m.role, content: m.content }]);
                }
            } catch {
                // ignore parse errors
            }
        });
        es.onerror = () => setSseState("error");
    }

    useEffect(() => {
        return () => esRef.current?.close();
    }, []);

    useEffect(() => {
        setAgentId(routeAgentId ?? "");
        setSessionId(routeSessionId ?? "");

        if (routeSessionId) {
            setSseState("idle");
            connectSse(routeSessionId);
        } else {
            esRef.current?.close();
            setSseState("idle");
        }
    }, [routeAgentId, routeSessionId]);

    // 初始化 建立agent和会话，建立sse监听
    async function bootstrap() {
        let nextAgentId = routeAgentId;
        if (!nextAgentId) {
            const createdAgent = await createAgent();
            nextAgentId = createdAgent.agentId;
        }

        const createdSession = await createSession(nextAgentId);
        setMessages([]);
        navigate(`/chat/${nextAgentId}/${createdSession.chatSessionId}`);
    }

    // 消息发送
    async function onSend() {
        if (!ready || !text.trim()) return;
        const content = text.trim();
        setMessages((prev) => [...prev, { sessionId, role: "user", content }]);
        setText("");
        await sendMessage({ agentId, sessionId, role: "user", content });
    }

    return (
        <div className="text-slate-900">
            <div className="mx-auto max-w-3xl p-2">
                <h1 className="text-2xl font-bold">聊天</h1>
                <p className="mt-1 text-xs text-slate-500">agentId: {agentId || "(未选择)"}</p>
                <p className="mt-1 text-xs text-slate-500">sessionId: {sessionId || "(未选择)"}</p>
                <p className="mt-1 text-sm text-slate-600">SSE: {sseState}</p>

                <div className="mt-4 flex gap-2">
                    <button onClick={bootstrap} className="rounded-lg bg-slate-900 px-4 py-2 text-white hover:bg-slate-700">
                        {agentId ? "新建会话并连接SSE" : "初始化 Agent + Session + SSE"}
                    </button>
                </div>

                <div className="mt-6 h-[55vh] overflow-y-auto rounded-xl bg-white p-4 shadow">
                    {messages.map((m, i) => (
                        <div key={`${m.id || i}`} className={`mb-3 flex ${m.role === "user" ? "justify-end" : "justify-start"}`}>
                            <div
                                className={`max-w-[80%] rounded-2xl px-3 py-2 text-sm ${
                                    m.role === "user" ? "bg-slate-900 text-white" : "bg-slate-200 text-slate-900"
                                }`}
                            >
                                {m.content}
                            </div>
                        </div>
                    ))}
                </div>

                <div className="mt-4 flex gap-2">
                    <input
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                        placeholder="输入消息..."
                        className="flex-1 rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none focus:border-slate-500"
                    />
                    <button onClick={onSend} disabled={!ready} className="rounded-lg bg-blue-600 px-4 py-2 text-white disabled:opacity-50">
                        发送
                    </button>
                </div>
            </div>
        </div>
    );
}
