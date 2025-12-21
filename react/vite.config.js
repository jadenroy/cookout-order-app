import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  css: {
    modules: {
      localsConvention: 'camelCase'
    },
  },
  plugins: [react()],
  server: {
    proxy: {
      '/cookouts': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },
      '/login': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },
      '/register': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },
      '/users': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})
