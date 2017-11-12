package com.woodpecker.handler;

import com.woodpecker.controller.UserController;
import com.woodpecker.utils.JWT;
import com.woodpecker.domain.User;
import com.woodpecker.service.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

// 处理json
import org.json.JSONObject;

import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

public class DemoWSHandler implements WebSocketHandler {
    private List<Timer> timer_list;
    private boolean token_checked = false;
    private WebSocketSession socketSession = null;
    private TimerTask timerTick= new TimerTask() {
        public void run() {
            if(null == socketSession)return;
            TextMessage message = new TextMessage("Tick!");
            try {
                socketSession.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    };

    @Resource
    private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("connect to the websocket success......");
        session.sendMessage(new TextMessage("Server:connected OK!"));
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        TextMessage returnMessage;
        JSONObject jsonObject = new JSONObject(wsm.getPayload().toString());

        System.out.println(jsonObject.toString());

        Integer id = jsonObject.getInt("id");
        String token = jsonObject.getString("token");

        User user = JWT.unsign(token, User.class);
        if (user != null && userService.getUser(user).getId().equals(id)) {
            //验证通过
            returnMessage = new TextMessage(userService.getUser(user).getUsername());
            try {
                Timer timer = new Timer(true);
                timer.schedule(timerTick, 1000, 1000);
                timer_list.add(timer);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            socketSession = wss;
        }
        else {
            returnMessage = new TextMessage("Verification failed");
        }

        //System.out.println(wss.getHandshakeHeaders().getFirst("Cookie"));

        wss.sendMessage(returnMessage);

    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            socketSession = null;
            wss.close();
        }
        for(Timer timer:timer_list){
            timer.cancel();
        }
        System.out.println("websocket connection closed due to error...");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        socketSession = null;
        System.out.println("websocket connection closed...");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
