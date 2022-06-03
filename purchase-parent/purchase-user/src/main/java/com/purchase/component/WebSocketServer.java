package com.purchase.component;

import com.alibaba.fastjson.JSON;
import com.purchase.service.impl.ChatService;
import com.purchase.vo.chat.param.SendMessParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{id}")
public class WebSocketServer {
    @Resource
    private ChatService chatService;

    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        sessionMap.put(id, session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        sessionMap.remove(id);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String mess, Session session, @PathParam("id") String id) throws IOException {
        System.out.println("收到信息:   "+mess);
        SendMessParam param = (SendMessParam) JSON.parse(mess);
        chatService.addMess(param);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
