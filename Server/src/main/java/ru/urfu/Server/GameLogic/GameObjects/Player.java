package ru.urfu.Server.GameLogic.GameObjects;

import java.util.Objects;

public class Player implements IGameObject, IPlayer {
    private String name;
    private int health;
    private String type = "player";
    private Direction direction = Direction.Up;
    private int killsCount =0;
    private int firedShotsCount = 0;
    private int movesCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private Player() {
    }

    public Player(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public Player(String name) {
        this.name = name;
        this.health = 100;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + type + "\"" +
                ",\"name\":\"" + name + '\"' +
                ",\"health\":" + health +
                ",\"direction\":\"" + direction +
                "\"}";
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public boolean canProjectilePass() {
        return false;
    }

    @Override
    public boolean canPlayerPass() {
        return false;
    }

    @Override
    public void hit(int damage) {
        health -= damage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMovesCount() {
        return movesCount;
    }

    @Override
    public void increaseMovesCount() {
        movesCount++;
    }

    @Override
    public int getFiredShotsCount() {
        return firedShotsCount;
    }

    @Override
    public void increaseFiredShotsCount() {
        firedShotsCount++;
    }

    @Override
    public int getKillsCount() {
        return killsCount;
    }

    @Override
    public void increaseKillsCount() {
        killsCount++;
    }
}
