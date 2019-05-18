package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
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
        Timer iterateTimer = new Timer(500, new IterateTimerListener());
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
        IPlayer player = playersPositions
                .keySet()
                .stream()
                .filter(p->p.getName().equals(playerAction.getPlayerName()))
                .findAny()
                .get();
        Point playerPosition = playersPositions.get(player);
        Direction playerDirection = player.getDirection();
        Point projectilePosition = null;
        IProjectile projectile = new Projectile(10, playerAction.getPlayerName());
        switch (playerDirection) {
            case Up:
                projectile.setDirection(Direction.Up);
                projectilePosition = new Point(playerPosition.x, playerPosition.y - 1);
                break;
            case Down:
                projectile.setDirection(Direction.Down);
                projectilePosition = new Point(playerPosition.x, playerPosition.y + 1);
                break;
            case Left:
                projectile.setDirection(Direction.Left);
                projectilePosition = new Point(playerPosition.x - 1, playerPosition.y);
                break;
            case Right:
                projectile.setDirection(Direction.Right);
                projectilePosition = new Point(playerPosition.x + 1, playerPosition.y);
                break;
        }
        projectilesPositions.put(projectile, projectilePosition);
    }

    private void removePlayer(PlayerAction playerAction) {
        playersPositions.entrySet().removeIf(e -> e.getKey().getName().equals(playerAction.getPlayerName()));
        if (playersPositions.size() == 0)
            gameState = GameState.WaitingForPlayers;
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
        if (map[newPos.x][newPos.y] == null || map[newPos.x][newPos.y].canPlayerPass())
            playersPositions.replace(player, newPos);

    }

    private class IterateTimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
