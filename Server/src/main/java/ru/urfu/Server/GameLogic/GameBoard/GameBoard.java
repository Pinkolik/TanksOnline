package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private IGameObject[][] map;
    private HashMap<IPlayer, Point> playersPositions = new HashMap<>();
    private HashMap<IProjectile, Point> projectilesPositions = new HashMap<>();
    private int width = 10;
    private int height = 10;
    private int playersCount = 0;
    private String type = "gameBoard";

    public GameBoard() {
        map = new IGameObject[width][height];
        for (int i = 0; i < width; i++) {
            map[i][0] = new Brick();
            map[i][height - 1] = new Brick();
        }
        for (int j = 1; j < height - 1; j++) {
            map[0][j] = new Brick();
            map[width - 1][j] = new Brick();
        }
        Timer iterateTimer = new Timer(100, new IterateTimerListener());
        iterateTimer.start();
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
    public HashMap<IPlayer, Point> getPlayersPositions() {
        return playersPositions;
    }

    @Override
    public HashMap<IProjectile, Point> getProjectilesPositions() {
        return projectilesPositions;
    }

    @Override
    public void processPlayerAction(PlayerAction playerAction) {
        PlayerActionEnum action = playerAction.getPlayerActionEnum();
        switch (action) {

            case Connected:
                putPlayerOnBoard(playerAction);
                break;
            case Disconnected:
                removePlayer(playerAction);
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
            case Update:
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
        IProjectile projectile = new Projectile(5, playerAction.getPlayerName());
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

        for (IPlayer player : playersPositions.keySet()) {
            if (player.getHealth() <= 0)
                playersPositions.remove(player);
        }
    }

    private void updateProjectilesPositions() {
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
                if (playerPointEntry.getValue() == newProjectilePosition) {
                    playerPointEntry.getKey().hit(projectile.getDamage());
                    projectilesPositions.remove(projectile);
                    continue;
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
    }

    private void removePlayer(PlayerAction playerAction) {
        playersPositions.entrySet().removeIf(e -> e.getKey().getName().equals(playerAction.getPlayerName()));
        if (playersPositions.size() == 0)
            gameState = GameState.WaitingForPlayers;
    }

    private boolean isOutOfBounds(Point point) {
        return point.x < 0 || point.x >= width || point.y < 0 || point.y >= height;
    }

    private void putPlayerOnBoard(PlayerAction playerAction) {
        gameState = GameState.InProgress;
        Random random = new Random();
        int x = 0;
        int y = 0;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (map[x][y] != null && !map[x][y].canPlayerPass());
        IPlayer player = new Player(playerAction.getPlayerName());
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
