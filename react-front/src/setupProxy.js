const proxy = require('http-proxy-middleware')
module.exports = function(app) {
  app.use(
    proxy('/URM/api', {
      target: 'http://localhost:9080',
      changeOrigin: true
    })
  )
}