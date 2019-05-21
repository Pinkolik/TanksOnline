package ru.urfu.Server.GameLogic.GameBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.Server.GameLogic.GameObjects.*;
import ru.urfu.Server.Models.Round;
import ru.urfu.Server.Models.User;
import ru.urfu.Server.Models.UserStatistics;
import ru.urfu.Server.Repositories.RoundsRepository;
import ru.urfu.Server.Repositories.UsersRepository;
import ru.urfu.Server.Repositories.UsersStatisticsRepository;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private HashSet<String> playersOnServer = new HashSet<>();
    private Round currentRound;
    private IGameObject[][] map;
    private HashMap<IPlayer, Point> playersPositions = new HashMap<>();
    private HashMap<IProjectile, Point> projectilesPositions = new HashMap<>();
    private int width = 10;
    private int height = 10;
    private String type = "gameBoard";
    private UUID gameToken;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoundsRepository roundsRepository;
    @Autowired
    private UsersStatisticsRepository usersStatisticsRepository;

    public GameBoard() {
        map = new IGameObject[width][height];
        gameToken = UUID.randomUUID();
        generateMap();
        Timer iterateTimer = new Timer(100, new IterateTimerListener());
        iterateTimer.start();
    }

    private void generateMap() {
        map = new IGameObject[width][height];
        Random random = new Random();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                switch (random.nextInt(6)) {
                    case 0:
                        map[i][j] = new Bush();
                        break;
                    case 1:
                        map[i][j] = new Water();
                        break;
                    case 2:
                        map[i][j] = new Rock();
                        break;
                    case 3:
                        map[i][j] = new Brick();
                        break;
                    case 4:
                        map[i][j] = null;
                        break;
                    case 5:
                        map[i][j] = null;
                        break;
                }
            }
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public IGameObject[][] getMap() {
        return map;
    }

    @Override
    public UUID getGameToken() {
        return gameToken;
    }

    @Override
    public HashMap<IPlayer, Point> getPlayersPositions() {
        return playersPositions;
    }

    @Override
    public HashMap<IProjectile, Point> getProjectilesPositions() {
        return projectilesPositions;
    }

    public void processPlayerAction(PlayerAction playerAction) {
        PlayerActionEnum action = playerAction.getPlayerActionEnum();
        switch (action) {

            case Connected:
                playerConnect(playerAction);
                break;
            case Disconnected:
                playerDisconnect(playerAction);
                break;
            case MoveLeft:
            case MoveRight:
            case MoveUp:
            case MoveDown:
                movePlayer(playerAction);
                break;
            case Shoot:
                putProjectileOnBoard(playerAction);
                break;
        }
    }

    private void putProjectileOnBoard(PlayerAction playerAction) {
        if (projectilesPositions.keySet().stream().anyMatch(p -> p.getOwner().equals(playerAction.getPlayerName())))
            return;
        IPlayer player = playersPositions
                .keySet()
                .stream()
                .filter(p -> p.getName().equals(playerAction.getPlayerName()))
                .findAny()
                .get();
        Point playerPosition = playersPositions.get(player);
        Direction playerDirection = player.getDirection();
        Point projectilePosition = new Point(playerPosition.x, playerPosition.y);
        IProjectile projectile = new Projectile(50, playerAction.getPlayerName());
        switch (playerDirection) {
            case Up:
                projectile.setDirection(Direction.Up);
                break;
            case Down:
                projectile.setDirection(Direction.Down);
                break;
            case Left:
                projectile.setDirection(Direction.Left);
                break;
            case Right:
                projectile.setDirection(Direction.Right);
                break;
        }
        projectilesPositions.put(projectile, projectilePosition);
        updateProjectilesPositions();
        updateMap();
    }

    private void updateMap() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (map[i][j] != null && map[i][j].getHealth() <= 0)
                    map[i][j] = null;
        HashMap<IPlayer, Point> newPlayersPositions = new HashMap<>();
        for (Map.Entry<IPlayer, Point> entry : playersPositions.entrySet()) {
            if (entry.getKey().getHealth() > 0)
                newPlayersPositions.put(entry.getKey(), entry.getValue());
            else {
                UserStatistics userStatistics = usersRepository.findByUserName(entry.getKey().getName()).getUserStatistics();
                userStatistics.setDeathsCount(userStatistics.getDeathsCount() + 1);
                usersStatisticsRepository.save(userStatistics);
            }
        }
        playersPositions = newPlayersPositions;
        if (playersPositions.size() < 2 && gameState == GameState.InProgress) {
            currentRound.setWinner(
                    usersRepository
                            .findByUserName(playersPositions
                                    .keySet()
                                    .stream()
                                    .findAny()
                                    .get()
                                    .getName())
                            .getUserStatistics());
            endRound();
        }
    }

    private void updateProjectilesPositions() {
        Set<String> playersToIncreaseKillsCount = new HashSet<>();
        for (Map.Entry<IProjectile, Point> entry : projectilesPositions.entrySet()) {
            IProjectile projectile = entry.getKey();
            Direction projectileDirection = projectile.getDirection();
            Point projectilePosition = entry.getValue();
            Point newProjectilePosition = null;
            switch (projectileDirection) {
                case Up:
                    newProjectilePosition = new Point(projectilePosition.x, projectilePosition.y - 1);
                    break;
                case Down:
                    newProjectilePosition = new Point(projectilePosition.x, projectilePosition.y + 1);
                    break;
                case Left:
                    newProjectilePosition = new Point(projectilePosition.x - 1, projectilePosition.y);
                    break;
                case Right:
                    newProjectilePosition = new Point(projectilePosition.x + 1, projectilePosition.y);
                    break;
            }
            if (isOutOfBounds(newProjectilePosition)) {
                projectilesPositions.remove(projectile);
                continue;
            }
            for (Map.Entry<IPlayer, Point> playerPointEntry : playersPositions.entrySet()) {
                if (playerPointEntry.getValue().equals(newProjectilePosition)) {
                    playerPointEntry.getKey().hit(projectile.getDamage());
                    if (playerPointEntry.getKey().getHealth() <= 0)
                        playersToIncreaseKillsCount.add(projectile.getOwner());
                    projectilesPositions.remove(projectile);
                }
            }

            IGameObject gameObject = map[newProjectilePosition.x][newProjectilePosition.y];
            if (gameObject == null || gameObject.canProjectilePass())
                projectilesPositions.replace(projectile, newProjectilePosition);
            else if (gameObject.isDestructible()) {
                gameObject.hit(projectile.getDamage());
                projectilesPositions.remove(projectile);
            } else
                projectilesPositions.remove(projectile);
        }
        for (IPlayer player : playersPositions.keySet())
            for (String playerNameToIncreaseKills : playersToIncreaseKillsCount)
                if (player.getName().equals(playerNameToIncreaseKills))
                    player.increaseKillsCount();
    }

    private void removePlayerFromBoard(String playerName) {
        playersPositions.entrySet().removeIf(e -> e.getKey().getName().equals(playerName));
    }

    private boolean isOutOfBounds(Point point) {
        return point.x < 0 || point.x >= width || point.y < 0 || point.y >= height;
    }

    private void startRound() {
        currentRound = new Round();
        roundsRepository.save(currentRound);
        for (User user : usersRepository.findAll())
            if (playersOnServer.contains(user.getUserName()))
                currentRound.getAllPlayers().add(user.getUserStatistics());
        generateMap();
        playersPositions = new HashMap<>();
        for (String playerName : playersOnServer)
            putPlayerOnBoard(playerName);
    }

    private void endRound() {
        if (currentRound != null) {
            currentRound.setRoundEnd(LocalDateTime.now());
            roundsRepository.save(currentRound);
            currentRound = null;
        }
        if (playersOnServer.size() >= 2)
            startRound();
        else
            gameState = GameState.WaitingForPlayers;
    }

    private void playerDisconnect(PlayerAction playerAction) {
        removePlayerFromBoard(playerAction.getPlayerName());
        playersOnServer.remove(playerAction.getPlayerName());
        if (playersOnServer.size() < 2)
            endRound();

    }

    private void playerConnect(PlayerAction playerAction) {
        String playerName = playerAction.getPlayerName();
        playersOnServer.add(playerName);
        if (gameState == GameState.WaitingForPlayers)
            putPlayerOnBoard(playerAction.getPlayerName());
        if (playersOnServer.size() >= 2)
            gameState = GameState.InProgress;
        if (gameState == GameState.InProgress && currentRound == null)
            startRound();
    }

    private void putPlayerOnBoard(String playerName) {
        //gameState = GameState.InProgress;
        Random random = new Random();
        int x = 0;
        int y = 0;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (map[x][y] != null && !map[x][y].canPlayerPass());
        IPlayer player = new Player(playerName);
        playersPositions.put(player, new Point(x, y));
    }

    private void movePlayer(PlayerAction playerAction) {
        IPlayer player = playersPositions
                .keySet()
                .stream()
                .filter(p -> p.getName().equals(playerAction.getPlayerName()))
                .findAny()
                .get();
        Point playerPosition = playersPositions.get(player);
        Point newPos = null;
        switch (playerAction.getPlayerActionEnum()) {
            case MoveUp:
                newPos = new Point(playerPosition.x, playerPosition.y - 1);
                player.setDirection(Direction.Up);
                break;
            case MoveDown:
                newPos = new Point(playerPosition.x, playerPosition.y + 1);
                player.setDirection(Direction.Down);
                break;
            case MoveLeft:
                newPos = new Point(playerPosition.x - 1, playerPosition.y);
                player.setDirection(Direction.Left);
                break;
            case MoveRight:
                newPos = new Point(playerPosition.x + 1, playerPosition.y);
                player.setDirection(Direction.Right);
                break;
        }
        if (isOutOfBounds(newPos))
            return;
        if (playersPositions.containsValue(newPos))
            return;
        if (map[newPos.x][newPos.y] == null || map[newPos.x][newPos.y].canPlayerPass())
            playersPositions.replace(player, newPos);

    }

    private class IterateTimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateProjectilesPositions();
            updateMap();
        }
    }
}
