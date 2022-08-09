// webpack.config.js
const webpack = require('webpack');
const WebpackShellPluginNext = require('webpack-shell-plugin-next');
console.log(new Date() + ' - Starting webpack loading...');

module.exports = {
  // usual config content
  resolve: {
    fallback: {
      "domain": require.resolve("domain-browser"),
      "buffer": require.resolve("buffer"),
      "events": require.resolve("events"),
      "util": require.resolve("util"),
      "process": require.resolve("process"),
      "path": require.resolve("path-browserify"),
      "vm": require.resolve("vm-browserify"),
      "https": require.resolve("https-browserify"), // needed by json-schema-ref-parser
      "http": require.resolve("http-browserify"), // needed by json-schema-ref-parser
      "stream": require.resolve("stream-browserify"),
    }
  },
  plugins: [
    new webpack.DefinePlugin({
        'process.env.NODE_DEBUG': JSON.stringify(false), // needed by json-schema-ref-parser
        'process.platform': JSON.stringify(null), // needed by json-schema-ref-parser
      }
    ),
    new WebpackShellPluginNext({
      swallowError: true,
      onBuildExit: {
        blocking: true,
        scripts: [
          'pwd',
          'cd ../event-tracker-backend/src/main/webapp && mkdir generated && move .\\*  generated'
        ]
      }
    })
  ]
}
