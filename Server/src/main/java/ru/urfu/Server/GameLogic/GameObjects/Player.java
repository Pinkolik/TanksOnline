package ru.urfu.Server.GameLogic.GameObjects;

public class Player implements IGameObject, IPlayer {

    private int id;
    private String name;
    private int health;

    public Player(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.health = 100;
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
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
