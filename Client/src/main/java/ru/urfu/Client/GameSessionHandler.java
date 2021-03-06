package ru.urfu.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;

import java.lang.reflect.Type;
import java.util.UUID;

public class GameSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(GameSessionHandler.class);
    private GameForm gameForm;
    private UUID gameToken;

    public GameSessionHandler(GameForm gameForm, UUID gameToken)
    {
        this.gameForm = gameForm;
        this.gameToken = gameToken;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/gameboard", this);
        logger.info("Subscribed to /topic/gameboard");
        session.send("/app/action", new PlayerAction(gameForm.getPlayerName(), PlayerActionEnum.Connected, gameToken));
        logger.info("Sent action to /app/action");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
        logger.info(new String(payload));
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return IGameBoard.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        IGameBoard gameBoard = (IGameBoard) payload;
        gameForm.updateGameBoard(gameBoard);
        logger.info("Received from server))))");
        logger.info(((IGameBoard) payload).getGameState());
    }
}