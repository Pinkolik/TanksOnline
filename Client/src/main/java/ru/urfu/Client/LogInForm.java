package ru.urfu.Client;

import ru.urfu.Server.AuthorizationReply;
import ru.urfu.Server.Models.User;

import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class LogInForm extends JFrame {
    private JPanel mainPanel;
    private JTextField ipTextField;
    private JTextField userNameTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private StompClient stompClient;
    private String authUrl = "/app/authorize";
    private UUID gameToken;

    public UUID getGameToken() {
        return gameToken;
    }

    public LogInForm() throws Exception {
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (userNameTextField.getText().equals("")
                        || passwordField.getPassword().length == 0
                        || ipTextField.getText().equals(""))
                    JOptionPane.showMessageDialog(null,
                            "Fields can't be empty",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                else if (stompClient.getStompSession() == null || !stompClient.getStompSession().isConnected()) {
                    try {
                        stompClient.connect(ipTextField.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        sendAuthorizeRequest();
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stompClient = new StompClient(new AuthorizationSessionHandler(this));
        setVisible(true);
    }

    public void processAuthorizationReply(AuthorizationReply authorizationReply) throws Exception {
        switch (authorizationReply.getAuthorizationStatus()) {
            case WrongPassword:
                JOptionPane.showMessageDialog(null,
                        "Wrong password!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case AlreadyLoggedIn:
                JOptionPane.showMessageDialog(null,
                        "Player is already playing!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case SuccessfulLogIn:
                JOptionPane.showMessageDialog(null,
                        "Successful log in",
                        "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
                this.gameToken = authorizationReply.getGameToken();
                new GameForm(userNameTextField.getText(), gameToken, ipTextField.getText());
                setVisible(false);
                dispose();
                break;
            case SuccessfulSignUp:
                JOptionPane.showMessageDialog(null,
                        "Created account successfully",
                        "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
                this.gameToken = authorizationReply.getGameToken();
                new GameForm(userNameTextField.getText(), gameToken, ipTextField.getText());
                setVisible(false);
                dispose();
                break;
        }
        System.out.println(authorizationReply.getAuthorizationStatus() + " " + authorizationReply.getGameToken());
    }

    public void sendAuthorizeRequest() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String userName = userNameTextField.getText();
        String hashedPassword = getMD5(passwordField.getPassword());
        User user = new User();
        user.setUserName(userName);
        user.setHashedPassword(hashedPassword);
        stompClient.getStompSession().send(authUrl, user);

    }

    private String getMD5(char[] password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(new String(password).getBytes("UTF-8"));
        String hashText = DatatypeConverter
                .printHexBinary(messageDigest).toUpperCase();
        return hashText;
    }

}
