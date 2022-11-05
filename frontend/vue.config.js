const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
});

module.exports = {
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
};
