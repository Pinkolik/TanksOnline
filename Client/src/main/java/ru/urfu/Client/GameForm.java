package ru.urfu.Client;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;
import ru.urfu.Server.GameLogic.GameObjects.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameForm extends JFrame {
    private HashMap<Class, BufferedImage> images = new HashMap<>();
    private StompClient stompClient;
    private String playerName = "Pinkolik";
    private String actionUrl = "/app/action";
    private IGameObject[][] map;
    private HashMap<IPlayer, Point> playersPositions;
    private HashMap<IProjectile, Point> projectilesPositions;
    private int width;
    private int height;
    private int blockWidth;
    private int blockHeight;
    private Timer updateTimer;

    private void initializeResources() throws Exception {
        Resource resource = new ClassPathResource("brick.png");
        images.put(Brick.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("player.png");
        images.put(Player.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("projectile.png");
        images.put(Projectile.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("bush.png");
        images.put(Bush.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("rock.png");
        images.put(Rock.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("water.png");
        images.put(Water.class, ImageIO.read(resource.getFile()));
    }

    public GameForm() throws Exception {
        playerName = Integer.toString(new Random().nextInt(1000));
        initializeResources();
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stompClient = new StompClient(this);
        addKeyListener(new MyKeyListener());
        addWindowListener(new MyWindowListener());
        updateTimer = new Timer(50, new TimerListener());
    }

    public void startTimer() {
        updateTimer.start();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void drawGameBoard(IGameBoard gameBoard) {
        updateDrawingSizes(gameBoard);
        Graphics g = this.getGraphics();
        drawMap(gameBoard, g);
        drawPlayers(gameBoard, g);
        drawProjectiles(gameBoard, g);

        g.dispose();
    }

    private void updateDrawingSizes(IGameBoard gameBoard) {
        IGameObject[][] newMap = gameBoard.getMap();
        width = newMap.length;
        height = newMap[0].length;
        blockWidth = getWidth() / width;
        blockHeight = getHeight() / height;
    }

    private void drawProjectiles(IGameBoard gameBoard, Graphics g) {
        IGameObject[][] newMap = gameBoard.getMap();
        HashMap<IProjectile, Point> newProjectilesPositions = gameBoard.getProjectilesPositions();
        if (projectilesPositions != null)
            for (Map.Entry<IProjectile, Point> entry : projectilesPositions.entrySet()) {
                Point point = entry.getValue();
                redrawBackgroundObject(g, newMap, point);
            }
        for (Map.Entry<IProjectile, Point> entry : newProjectilesPositions.entrySet()) {
            Point point = entry.getValue();
            IProjectile projectile = entry.getKey();
            g.drawImage(rotateImage(images.get(projectile.getClass()), projectile.getDirection()),
                    point.x * blockWidth, point.y * blockHeight,
                    blockWidth, blockHeight, null);
        }
        projectilesPositions = newProjectilesPositions;
    }

    private void redrawBackgroundObject(Graphics g, IGameObject[][] newMap, Point previousPoint) {
        g.clearRect(previousPoint.x * blockWidth, previousPoint.y * blockHeight, blockWidth, blockHeight);
        if (newMap[previousPoint.x][previousPoint.y] != null)
            g.drawImage(rotateImage(images.get(newMap[previousPoint.x][previousPoint.y].getClass()),
                    newMap[previousPoint.x][previousPoint.y].getDirection()),
                    previousPoint.x * blockWidth, previousPoint.y * blockHeight,
                    blockWidth, blockHeight, null);
    }

    private void drawPlayers(IGameBoard gameBoard, Graphics g) {
        HashMap<IPlayer, Point> newPlayersPosition = gameBoard.getPlayersPositions();
        IGameObject[][] newMap = gameBoard.getMap();
        for (Map.Entry<IPlayer, Point> entry : newPlayersPosition.entrySet()) {
            Point point = entry.getValue();
            IPlayer player = entry.getKey();
            if (playersPositions != null) {
                Point previousPoint = playersPositions.get(player);
                IPlayer previousPlayer = playersPositions
                        .keySet()
                        .stream()
                        .filter(p -> p.getName().equals(player.getName()))
                        .findAny()
                        .get();
                if (!previousPoint.equals(point) || player.getDirection() != previousPlayer.getDirection()) {
                    redrawBackgroundObject(g, newMap, previousPoint);
                    g.drawImage(rotateImage(images.get(player.getClass()), player.getDirection()),
                            point.x * blockWidth, point.y * blockHeight,
                            blockWidth, blockHeight, null);
                }

            } else
                g.drawImage(rotateImage(images.get(player.getClass()), player.getDirection()),
                        point.x * blockWidth, point.y * blockHeight,
                        blockWidth, blockHeight, null);
        }
        playersPositions = newPlayersPosition;
    }

    private void drawMap(IGameBoard gameBoard, Graphics g) {
        IGameObject[][] newMap = gameBoard.getMap();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                if (map != null) {
                    if (map[i][j] != null && newMap[i][j] == null)
                        g.clearRect(i * blockWidth, j * blockHeight, blockWidth, blockHeight);
                    if (map[i][j] == null && newMap[i][j] != null)
                        g.drawImage(rotateImage(images.get(newMap[i][j].getClass()), newMap[i][j].getDirection()),
                                i * blockWidth, j * blockHeight,
                                blockWidth, blockHeight, null);
                    if (map[i][j] != null && newMap[i][j] != null
                            && (map[i][j].getClass() != newMap[i][j].getClass()
                            || map[i][j].getDirection() != newMap[i][j].getDirection())) {
                        g.clearRect(i * blockWidth, j * blockHeight, blockWidth, blockHeight);
                        g.drawImage(rotateImage(images.get(newMap[i][j].getClass()), newMap[i][j].getDirection()),
                                i * blockWidth, j * blockHeight,
                                blockWidth, blockHeight, null);
                    }
                } else if (newMap[i][j] != null)
                    g.drawImage(rotateImage(images.get(newMap[i][j].getClass()), newMap[i][j].getDirection()),
                            i * blockWidth, j * blockHeight,
                            blockWidth, blockHeight, null);
            }
        map = newMap;
    }

    private static BufferedImage rotateImage(BufferedImage src, Direction direction) {
        int width = src.getWidth();
        int height = src.getHeight();
        double angle = 0;
        switch (direction) {
            case Up:
                angle = Math.PI;
                break;
            case Down:
                return src;
            case Left:
                angle = Math.PI / 2;
                break;
            case Right:
                angle = -Math.PI / 2;
                break;
        }

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(angle, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);
        graphics2D.dispose();
        return dest;
    }

    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (Character.toLowerCase(e.getKeyChar())) {
                case 'w':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveUp));
                    break;
                case 's':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveDown));
                    break;
                case 'a':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveLeft));
                    break;
                case 'd':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveRight));
                    break;
                case ' ':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.Shoot));
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            stompClient
                    .getStompSession()
                    .send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.Update));
        }
    }

    private class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.Disconnected));
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
