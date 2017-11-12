package com.woodpecker.handler;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.woodpecker.utils.JWT;
import com.woodpecker.domain.User;
import com.woodpecker.domain.SocketSession;
import com.woodpecker.service.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

// 处理json
import org.json.JSONObject;

import javax.annotation.Resource;
import java.util.Map;
import java.util.HashMap;

public class DemoWSHandler implements WebSocketHandler {
    private boolean token_checked = false;
    Map<WebSocketSession, SocketSession> sessionManager = new HashMap<WebSocketSession, SocketSession>();
    @Resource
    private UserService userService;

    @Override
    //建立连接
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");
        session.sendMessage(new TextMessage("Server:connected OK!"));
    }

    @Override
    //接收到消息
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        TextMessage returnMessage;
        JSONObject jsonObject = new JSONObject(wsm.getPayload().toString());

        //System.out.println(jsonObject.toString());

        Integer id = (Integer)jsonObject.get("id");
        String token = (String)jsonObject.get("token");
        String type = (String)jsonObject.get("type");
        String keywordName = (String)jsonObject.get("name");

        User user = JWT.unsign(token, User.class);
        if (user != null && userService.getUser(user).getId().equals(id)) {
            //验证通过
            SocketSession socketSession;
            switch(type) {
                case "play":
                    //开始定时发送数据
                    System.out.println("play get");
                    socketSession = sessionManager.get(wss);
                    if(null==socketSession) { //Manager中没找到，添加
                        System.out.println("Creating a new socketSession");
                        socketSession = new SocketSession(wss, keywordName);
                        sessionManager.put(wss, socketSession);
                    }
                    socketSession.Play();
                    returnMessage = new TextMessage("Play success");
                    break;
                case "pause":
                    //暂停定时发送数据
                    System.out.println("pause get");
                    socketSession = sessionManager.get(wss);
                    if(null==socketSession) { //Manager中没找到，添加
                        System.out.println("Creating a new socketSession");
                        socketSession = new SocketSession(wss, keywordName);
                        sessionManager.put(wss, socketSession);
                    }
                    socketSession.Pause();
                    returnMessage = new TextMessage("Pause success");
                    break;
                default:
                    System.out.println("Unknown type");
                    returnMessage = new TextMessage("Unknown type");
                    break;
            }
        }
        else {
            returnMessage = new TextMessage("Verification failed");
        }

        //System.out.println(wss.getHandshakeHeaders().getFirst("Cookie"));

        wss.sendMessage(returnMessage);

    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        sessionManager.get(wss).Pause();
        sessionManager.remove(wss);
        if(wss.isOpen()){
            wss.close();
        }
        System.out.println("websocket connection closed due to error...");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("Removing session...");
        sessionManager.get(wss).Pause();
        sessionManager.remove(wss);
        System.out.println("websocket connection closed...");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
