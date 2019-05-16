package ru.urfu.Server;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.urfu.Server.GameLogic.GameBoard.GameBoard;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;

@Controller
public class GameController {

    private IGameBoard gameBoard = new GameBoard();

    @MessageMapping("/action")
    @SendTo("/topic/gameboard")
    public IGameBoard makeAction(PlayerAction playerAction)
    {
        System.out.println("Received from client " + playerAction.name());
        return gameBoard;
    }

}
