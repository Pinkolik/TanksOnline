package ru.urfu.Client;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.urfu.Server.GameLogic.GameBoard.IGameBoard;
import ru.urfu.Server.GameLogic.GameBoard.PlayerAction;
import ru.urfu.Server.GameLogic.GameBoard.PlayerActionEnum;
import ru.urfu.Server.GameLogic.GameObjects.Brick;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameForm extends JFrame {
    private BufferedImage brickImage;
    private BufferedImage playerImage;
    private StompClient stompClient;


    private void initializeResources() throws Exception {
        Resource resource = new ClassPathResource("brick.png");
        brickImage = ImageIO.read(resource.getFile());
        resource = new ClassPathResource("player.png");
        playerImage = ImageIO.read(resource.getFile());
    }

    public GameForm() throws Exception {
        initializeResources();
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w')
                    stompClient.getStompSession().send("/app/action", new PlayerAction("Pinkolik", PlayerActionEnum.MoveUp));
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });
        stompClient = new StompClient(this);
        Timer updateTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stompClient
                        .getStompSession()
                        .send("/app/action", new PlayerAction("Pinkolik", PlayerActionEnum.Update));
            }
        });
        updateTimer.start();
    }

    public void drawGameBoard(IGameBoard gameBoard) {
        IGameObject[][] map = gameBoard.getMap();
        int width = map.length;
        int height = map[0].length;
        int blockWidth = getWidth() / width;
        int blockHeight = getHeight() / height;
        Graphics g = this.getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                IGameObject gameObject = map[i][j];
                if (gameObject instanceof Brick) {
                    g.drawImage(brickImage, i * blockWidth, j * blockHeight, blockWidth, blockHeight, null);
                } else if (gameObject instanceof Player) {
                    g.drawImage(playerImage, i * blockWidth, j * blockHeight, blockWidth, blockHeight, null);
                }
            }
    }
}
