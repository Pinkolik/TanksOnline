package ru.urfu.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.urfu.Server.GameLogic.GameBoard.GameBoard;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/gameboard", this);
        logger.info("Subscribed to /topic/gameboard");
        session.send("/app/action", PlayerAction.Connected);
        logger.info("Sent action to /app/action");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameBoard.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        IGameBoard gameBoard = (GameBoard) payload;
        logger.info("Received from server))))");
        logger.info(((GameBoard) payload).getGameState());
    }
}