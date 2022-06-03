const path = require('path')

function resolve(dir) {
    return path.join(__dirname, dir)
}


// let proxyObj = {};
// proxyObj["/api"] = {
//     pathRewrite: {
//         '^/api': ''
//     },
//     // ws: false, // 关闭 webSocket
//     target: "http://localhost:9990", // 后端的地址
//     changeOrigin: true,
// }

module.exports = {
    devServer: {
        port: 8889,
        proxy: {
            '/api': {
                pathRewrite: {
                    '^/api': ''
                },
                ws: false, // 关闭 webSocket
                target: "http://localhost:9990", // 后端的地址
                changeOrigin: true,
            },
            '/file': {
                // pathRewrite: {
                //     '^/file': ''
                // },
                ws: false, // 关闭 webSocket
                target: "http://localhost:9991", // 后端的地址
                changeOrigin: true,
            },
        },
    },
    chainWebpack: config => {
        config.module
            .rule('svg')
            .exclude.add(resolve('./src/static/iconsvg/'))
            .end()
        config.module
            .rule('svg-sprite')
            .test(/\.svg$/)
            // 处理svg目录
            .include.add(resolve('./src/static/iconsvg/'))
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
                symbolId: 'icon-[name]'
            })
            .end()
            .before('svg-sprite-loader')
            .use('svgo-loader')
            .loader('svgo-loader')
            .options({
                plugins: [
                    {
                        name: 'removeAttrs',
                        params: {
                            attrs: '(fill|stroke)'
                        }
                    },
                    {
                        name: 'removeTitle'
                    }
                ]
            })
            .end()
    }

}