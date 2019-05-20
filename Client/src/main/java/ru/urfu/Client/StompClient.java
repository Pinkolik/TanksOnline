package ru.urfu.Client;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class StompClient {

    private Logger logger = LogManager.getLogger(StompClient.class);
    private StompSessionHandlerAdapter sessionHandler;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    public StompClient(StompSessionHandlerAdapter sessionHandler) throws Exception {
        WebSocketClient client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        this.sessionHandler = sessionHandler;
    }

    public void connect(String ip) throws Exception {
        String URL = "ws://" + ip + ":8080/game";
        stompSession = stompClient.connect(URL, sessionHandler).get();
    }

    public StompSession getStompSession() {
        return stompSession;
    }
}
