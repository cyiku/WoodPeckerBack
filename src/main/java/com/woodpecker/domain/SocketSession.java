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
    Integer pkgId;
    private class SessionTimerTask extends TimerTask {
        @Override
        public void run() {
            if(null == session)
            {
                System.out.println("Error: null session");
                return;
            }
            pkgId++;
            TextMessage message = new TextMessage( pkgId + ":" + keywordName);
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    public SocketSession (WebSocketSession session,String keywordName) {
        timer = null;
        pkgId=0;
        this.session=session;
        this.keywordName=keywordName;
    }

    public void Play() {
        if(null!=timer) {
            timer.cancel();
        }
        timer = new Timer();
        SessionTimerTask timerTick = new SessionTimerTask();
        timer.schedule(timerTick, 1000, 2500);
    }

    public void Pause() {
        if(null==timer)return;
        timer.cancel();
    }
}
