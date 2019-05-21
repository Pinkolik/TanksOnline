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
    private String userName;

    public String getUserName() {
        return userName;
    }

    public UUID getGameToken() {
        return gameToken;
    }

    LogInForm() throws Exception {
        super("Tanks Online Login Form");
        setContentPane(mainPanel);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!userNameTextField.getText().equals(""))
                    userName = userNameTextField.getText();
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
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stompClient = new StompClient(new AuthorizationSessionHandler(this));
        setVisible(true);
    }

    public void processAuthorizationReply(AuthorizationReply authorizationReply) throws Exception {
        switch (authorizationReply.getAuthorizationStatus()) {
            case WrongPassword:
                stompClient.getStompSession().disconnect();
                JOptionPane.showMessageDialog(null,
                        "Wrong password!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case AlreadyLoggedIn:
                stompClient.getStompSession().disconnect();
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
                new GameForm(userName, gameToken, ipTextField.getText());
                stompClient.getStompSession().disconnect();
                setVisible(false);
                dispose();
                break;
            case SuccessfulSignUp:
                JOptionPane.showMessageDialog(null,
                        "Created account successfully",
                        "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
                this.gameToken = authorizationReply.getGameToken();
                new GameForm(userName, gameToken, ipTextField.getText());
                stompClient.getStompSession().disconnect();
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
        stompClient.getStompSession().send("/app/" + userName + "/authorize", user);

    }

    private String getMD5(char[] password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(new String(password).getBytes("UTF-8"));
        String hashText = DatatypeConverter
                .printHexBinary(messageDigest).toUpperCase();
        return hashText;
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("IP");
        mainPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ipTextField = new JTextField();
        mainPanel.add(ipTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Username");
        mainPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userNameTextField = new JTextField();
        mainPanel.add(userNameTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Password");
        mainPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setText("Login");
        mainPanel.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
