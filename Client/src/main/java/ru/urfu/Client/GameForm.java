package ru.urfu.Client;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;
import ru.urfu.Server.GameLogic.GameObjects.Brick;
import ru.urfu.Server.GameLogic.GameObjects.Direction;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

public class GameForm extends JFrame {
    private HashMap<Class, BufferedImage> images = new HashMap<>();
    private BufferedImage brickImage;
    private BufferedImage playerImage;
    private StompClient stompClient;
    private String playerName = "Pinkolik";
    private String actionUrl = "/app/action";
    private IGameObject[][] map;

    private void initializeResources() throws Exception {
        Resource resource = new ClassPathResource("brick.png");
        images.put(Brick.class, ImageIO.read(resource.getFile()));
        resource = new ClassPathResource("player.png");
        images.put(Player.class, ImageIO.read(resource.getFile()));
    }

    public GameForm() throws Exception {
        initializeResources();
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stompClient = new StompClient(this);
        addKeyListener(new MyKeyListener());
        addWindowListener(new MyWindowListener());
        Timer updateTimer = new Timer(100, new TimerListener());
        updateTimer.start();
    }

    public void drawGameBoard(IGameBoard gameBoard) {
        IGameObject[][] newMap = gameBoard.getMap();
        int width = newMap.length;
        int height = newMap[0].length;
        int blockWidth = getWidth() / width;
        int blockHeight = getHeight() / height;
        Graphics g = this.getGraphics();
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
        g.dispose();
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
