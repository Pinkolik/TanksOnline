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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameForm extends JFrame {
    private String ip;
    private UUID gameToken;
    private HashMap<Class, BufferedImage> images = new HashMap<>();
    private StompClient stompClient;
    private String playerName;
    private String actionUrl = "/app/action";
    private IGameBoard gameBoard;
    private JPanel jPanel;
    private int width;
    private int height;
    private int blockWidth;
    private int blockHeight;

    private void initializeResources() throws Exception {
        Resource resource = new ClassPathResource("brick.png");
        images.put(Brick.class, ImageIO.read(resource.getInputStream()));
        resource = new ClassPathResource("player.png");
        images.put(Player.class, ImageIO.read(resource.getInputStream()));
        resource = new ClassPathResource("projectile.png");
        images.put(Projectile.class, ImageIO.read(resource.getInputStream()));
        resource = new ClassPathResource("bush.png");
        images.put(Bush.class, ImageIO.read(resource.getInputStream()));
        resource = new ClassPathResource("rock.png");
        images.put(Rock.class, ImageIO.read(resource.getInputStream()));
        resource = new ClassPathResource("water.png");
        images.put(Water.class, ImageIO.read(resource.getInputStream()));
    }

    GameForm(String playerName, UUID gameToken, String ip) throws Exception {
        this.playerName = playerName;
        this.gameToken = gameToken;
        this.ip = ip;
        initializeResources();
        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gameBoard == null)
                    return;
                drawHashMap(g, gameBoard.getPlayersPositions());
                drawMap(g);
                drawHashMap(g, gameBoard.getProjectilesPositions());
            }

        };
        getContentPane().add(jPanel);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stompClient = new StompClient(new GameSessionHandler(this, gameToken));
        addKeyListener(new MyKeyListener());
        addWindowListener(new MyWindowListener());
        stompClient.connect(ip);
    }

    String getPlayerName() {
        return playerName;
    }

    void updateGameBoard(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
        updateDrawingSizes(gameBoard);
        jPanel.repaint();
    }

    private void updateDrawingSizes(IGameBoard gameBoard) {
        IGameObject[][] newMap = gameBoard.getMap();
        width = newMap.length;
        height = newMap[0].length;
        blockWidth = jPanel.getWidth() / width;
        blockHeight = jPanel.getHeight() / height;
    }


    private void drawHashMap(Graphics g, HashMap<? extends IGameObject, Point> hashMap) {
        if (hashMap == null)
            return;
        for (Map.Entry<? extends IGameObject, Point> entry : hashMap.entrySet()) {
            Point point = entry.getValue();
            IGameObject iGameObject = entry.getKey();
            g.drawImage(rotateImage(images.get(iGameObject.getClass()), iGameObject.getDirection()),
                    point.x * blockWidth, point.y * blockHeight,
                    blockWidth, blockHeight, null);
        }
    }

    private void drawMap(Graphics g) {
        IGameObject[][] map = gameBoard.getMap();
        if (map == null)
            return;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (map[i][j] != null)
                    g.drawImage(images.get(map[i][j].getClass()),
                            i * blockWidth, j * blockHeight,
                            blockWidth, blockHeight, null);
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

        BufferedImage dest = new BufferedImage(width, height, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(angle, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);
        graphics2D.dispose();
        return dest;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }

    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (Character.toLowerCase(e.getKeyChar())) {
                case 'w':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveUp, gameToken));
                    break;
                case 's':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveDown, gameToken));
                    break;
                case 'a':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveLeft, gameToken));
                    break;
                case 'd':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.MoveRight, gameToken));
                    break;
                case ' ':
                    stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.Shoot, gameToken));
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            stompClient.getStompSession().send(actionUrl, new PlayerAction(playerName, PlayerActionEnum.Disconnected, gameToken));
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
