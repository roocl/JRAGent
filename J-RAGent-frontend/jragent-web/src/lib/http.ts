import axios from "axios";
import type { ApiResponse } from "../types/api";
// axios实例包装
export const http = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "",
    timeout: 15000,
});
// 泛型拆包函数
export async function unwrap<T>(promise: Promise<{ data: ApiResponse<T> }>): Promise<T> {
    const res = await promise;
    // 只有code为200时才不报错
    if (res.data.code !== 200) throw new Error(res.data.message || "Request failed");
    return res.data.data;
}
