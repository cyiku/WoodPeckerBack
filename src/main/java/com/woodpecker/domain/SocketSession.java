package com.woodpecker.domain;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class SocketSession {
    private Timer timer;
    private WebSocketSession session;
    private String keywordName;
    private TimerTask timerTick= new TimerTask() {
        public void run() {
            if(null == session)
            {
                System.out.println("Error: null session");
                return;
            }
            TextMessage message = new TextMessage(keywordName);
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    };
    public SocketSession (WebSocketSession session,String keywordName) {
        timer = new Timer(true);
        this.session=session;
        this.keywordName=keywordName;
        timer.schedule(timerTick,1000,5000);
    }

    public void Stop() {
        timer.cancel();
    }
}
