package ru.urfu.Server.GameLogic.GameBoard;

public class PlayerAction {
    private String playerName;
    private PlayerActionEnum playerActionEnum;

    public PlayerAction(String playerName, PlayerActionEnum playerActionEnum) {
        this.playerName = playerName;
        this.playerActionEnum = playerActionEnum;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerActionEnum getPlayerActionEnum() {
        return playerActionEnum;
    }
}
