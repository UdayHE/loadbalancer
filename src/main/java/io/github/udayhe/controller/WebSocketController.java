package io.github.udayhe.controller;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.ServerWebSocket;

@ServerWebSocket("/ws/{topic}")
public class WebSocketController {

    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        session.sendSync("Received: " + message);
    }
}
