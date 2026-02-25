import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from "@tailwindcss/vite";

// https://vite.dev/config/
export default defineConfig({
  // 启用插件
  plugins: [react(), tailwindcss()],
  // 开发服务器代理，转发请求到指定url，解决跨域问题
  server: {
    proxy: {
      "/api": "http://127.0.0.1:8080",
      "/sse": "http://127.0.0.1:8080",
    },
  },
});
