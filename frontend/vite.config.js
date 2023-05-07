import vue from '@vitejs/plugin-vue'
import path from 'path'

export default {
  plugins: [vue()],
  devServer: {
    port: 8080,
    proxy: {
      '^/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        pathRewrite: { '^/api': '/' },
      },
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
}
