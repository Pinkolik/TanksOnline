package ru.urfu.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import ru.urfu.Server.GameLogic.GameBoard.GameBoard;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;

@EnableScheduling
@Controller
public class GameController {

    private IGameBoard gameBoard = new GameBoard();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/action")
    @SendTo("/topic/gameboard")
    public IGameBoard makeAction(PlayerAction playerAction) {
        System.out.println("Received from client " + playerAction.getPlayerName() + " " + playerAction.getPlayerActionEnum().name());
        gameBoard.processPlayerAction(playerAction);
        return gameBoard;
    }

    @Scheduled(fixedDelay = 50)
    public void sendUpdateToSubscribers()
    {
        simpMessagingTemplate.convertAndSend("/topic/gameboard", gameBoard);
    }

}
