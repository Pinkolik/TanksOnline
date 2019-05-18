package ru.urfu.Server.GameLogic.GameObjects;

public class Projectile implements IProjectile {
    private int damage;
    private Direction direction;
    private String type = "projectile";
    private String owner;

    private Projectile() {
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + type + '\"' +
                ",\"damage\":" + damage +
                ",\"direction\":\"" + direction + '\"' +
                ",\"owner\":\"" + owner + '\"' +
                '}';
    }

    public Projectile(int damage, String owner) {
        this.damage = damage;
        this.owner = owner;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public String getOwner() {
        return owner;
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
        return 0;
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public boolean canProjectilePass() {
        return true;
    }

    @Override
    public boolean canPlayerPass() {
        return false;
    }

    @Override
    public void hit(int damage) {

    }
}
