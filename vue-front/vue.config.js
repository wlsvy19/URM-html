const webpack = require("webpack");

module.exports = {
  outputDir : '../build/resources/main/static/',
  publicPath : '/',
  assetsDir : 'assets',

  devServer : {
    port : 8080,
    proxy : {
      '/api' : {
        target : 'http://localhost:9080/URM',
        changeOrigin : true,
      },
    }
  },

};
