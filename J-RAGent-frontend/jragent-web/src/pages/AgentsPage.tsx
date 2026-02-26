import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { listAgents } from "../api/agents";

export default function AgentsPage() {
  const navigate = useNavigate();
  const { data, isLoading, isError, refetch } = useQuery({
    queryKey: ["agents"],
    queryFn: listAgents,
  });

  const agents = data?.agents ?? [];

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">智能体</h1>
        <button
          onClick={() => refetch()}
          className="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 hover:bg-slate-50"
        >
          刷新
        </button>
      </div>

      {isLoading && <div className="rounded-xl bg-white p-4 text-sm text-slate-600">正在加载智能体列表...</div>}

      {isError && (
        <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">加载失败，请稍后重试。</div>
      )}

      {!isLoading && !isError && agents.length === 0 && (
        <div className="rounded-xl bg-white p-6 text-sm text-slate-600">暂无智能体。下一步可在 D 部分加入“新建智能体”。</div>
      )}

      {!isLoading && !isError && agents.length > 0 && (
        <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
          {agents.map((agent) => (
            <button
              key={agent.id}
              onClick={() => navigate(`/chat/${agent.id}`)}
              className="rounded-xl border border-slate-200 bg-white p-4 text-left shadow-sm transition hover:border-slate-300 hover:shadow"
            >
              <div className="text-base font-semibold">{agent.name}</div>
              <div className="mt-1 text-sm text-slate-600">{agent.description}</div>
              <div className="mt-3 text-xs text-slate-500">model: {String(agent.model)}</div>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
