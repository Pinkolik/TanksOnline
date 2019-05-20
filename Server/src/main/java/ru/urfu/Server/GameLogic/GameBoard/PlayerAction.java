package ru.urfu.Server.GameLogic.GameBoard;

import java.util.UUID;

public class PlayerAction {
    private String playerName;
    private PlayerActionEnum playerActionEnum;
    private UUID gameToken;

    public PlayerAction(String playerName, PlayerActionEnum playerActionEnum, UUID gameToken) {
        this.playerName = playerName;
        this.playerActionEnum = playerActionEnum;
        this.gameToken = gameToken;
    }

    public UUID getGameToken() {
        return gameToken;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerActionEnum getPlayerActionEnum() {
        return playerActionEnum;
    }
}
