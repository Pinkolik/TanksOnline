package ru.urfu.Server;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import ru.urfu.Server.Repositories.RoundsRepository;
import ru.urfu.Server.Repositories.UsersRepository;
import ru.urfu.Server.Repositories.UsersStatisticsRepository;

import javax.websocket.server.PathParam;

@EnableScheduling
@Controller
public class GameController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersStatisticsRepository userStatisticsRepository;
    @Autowired
    private RoundsRepository roundsRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private IGameBoard gameBoard;

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

    @MessageMapping("/{username}/authorize")
    @SendTo("/user/{username}/auth_reply")
    public AuthorizationReply authorize(User user) {
        System.out.println("Authorization request");
        if (gameBoard.getPlayersPositions().keySet().stream().anyMatch(p -> p.getName().equals(user.getUserName())))
            return new AuthorizationReply(AuthorizationStatus.AlreadyLoggedIn, null);
        User find = usersRepository.findByUserNameAndAndHashedPassword(user.getUserName(), user.getHashedPassword());
        if (find != null)
            return new AuthorizationReply(AuthorizationStatus.SuccessfulLogIn, gameBoard.getGameToken());
        else if (usersRepository.findByUserName(user.getUserName()) != null)
            return new AuthorizationReply(AuthorizationStatus.WrongPassword, null);
        else {
            UserStatistics userStatistics = new UserStatistics();
            userStatistics.setUser(user);
            user.setUserStatistics(userStatistics);
            userStatisticsRepository.save(userStatistics);
            usersRepository.save(user);
            return new AuthorizationReply(AuthorizationStatus.SuccessfulSignUp, gameBoard.getGameToken());
        }

    }

}
