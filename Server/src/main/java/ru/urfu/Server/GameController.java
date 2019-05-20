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
import ru.urfu.Server.Models.User;
import ru.urfu.Server.Models.UserStatistics;
import ru.urfu.Server.Repositories.UsersRepository;

@EnableScheduling
@Controller
public class GameController {

    private IGameBoard gameBoard = new GameBoard();
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/action")
    @SendTo("/topic/gameboard")
    public IGameBoard makeAction(PlayerAction playerAction) {
        if (!playerAction.getGameToken().equals(gameBoard.getGameToken()))
            return null;
        System.out.println("Received from client " + playerAction.getPlayerName() + " " + playerAction.getPlayerActionEnum().name());
        gameBoard.processPlayerAction(playerAction);
        return gameBoard;
    }

    @Scheduled(fixedDelay = 50)
    public void sendUpdateToSubscribers() {
        simpMessagingTemplate.convertAndSend("/topic/gameboard", gameBoard);
    }

    @MessageMapping("/authorize")
    @SendTo("/topic/auth_reply")
    public AuthorizationReply authorize(User user) {
        if (gameBoard.getPlayersPositions().keySet().stream().anyMatch(p -> p.getName().equals(user.getUserName())))
            return new AuthorizationReply(AuthorizationStatus.AlreadyLoggedIn, null);
        User find = usersRepository.findByUserNameAndAndHashedPassword(user.getUserName(), user.getHashedPassword());
        if (find != null)
            return new AuthorizationReply(AuthorizationStatus.SuccessfulLogIn, gameBoard.getGameToken());
        else if (usersRepository.findByUserName(user.getUserName()) != null)
            return new AuthorizationReply(AuthorizationStatus.WrongPassword, null);
        else {
            usersRepository.save(user);
            return new AuthorizationReply(AuthorizationStatus.SuccessfulSignUp, gameBoard.getGameToken());
        }

    }

}
