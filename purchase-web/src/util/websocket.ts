

const socket: WebSocket = new WebSocket("ws://localhost:8082/chat/");//这里就会去连接
socket.onopen = () => {
    console.log("连接成功了")
    //连接成功后执行发送token作为服务的识别会话
    // socket.send(JSON.stringify({ Authorization: localStorage.getItem('Authorization') }))
}
socket.onmessage = function (msg: any) {
    //接收消息
    console.log("接收到了消息  ", msg)
};
socket.onclose = function () {
    console.log("websocket已关闭");
};
socket.onerror = function () {
    console.log("websocket发生了错误");
}

