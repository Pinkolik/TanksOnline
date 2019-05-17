package ru.urfu.Server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.urfu.Server.GameLogic.GameBoard.GameBoard;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;

@Controller
public class GameController {

    private IGameBoard gameBoard = new GameBoard();

    @MessageMapping("/action")
    @SendTo("/topic/gameboard")
    public IGameBoard makeAction(PlayerAction playerAction) {
        System.out.println("Received from client " + playerAction.getPlayerName() + " " + playerAction.getPlayerActionEnum().name());
        gameBoard.processPlayerAction(playerAction);
        return gameBoard;
    }

}
