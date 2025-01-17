package io.github.udayhe.controller;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.ServerWebSocket;

import static io.github.udayhe.constant.ConfigConstant.WEBSOCKET_PATH;

@ServerWebSocket(WEBSOCKET_PATH)
public class WebSocketController {

    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        session.sendSync("Received: " + message);
    }
}
