package ru.urfu.Server;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.urfu.Server.GameLogic.GameBoard.GameBoard;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;

@Controller
public class GameController {

    @MessageMapping("/action")
    @SendTo("/topic/gameboard")
    public IGameBoard makeAction(String playerName, PlayerAction action)
    {
        return null;
    }

}
