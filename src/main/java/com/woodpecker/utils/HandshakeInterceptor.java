package com.woodpecker.utils;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        boolean ans = false;
        try {
            // 解决The extension [x-webkit-deflate-frame] is not supported问题
            if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
                request.getHeaders().set("Sec-WebSocket-Extensions",
                        "permessage-deflate");
            }
            ans = super.beforeHandshake(request, response, wsHandler, attributes);
            System.out.println("Before Handshake");
        } catch (Exception e) {
            System.out.println(e);
        }
        return ans;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        try {
            System.out.println("After Handshake");
            super.afterHandshake(request, response, wsHandler, ex);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}