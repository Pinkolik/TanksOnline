package ru.urfu.Server.GameLogic.GameObjects;

public interface IGameObject {
    boolean IsDestructible = false;
    boolean CanProjectilePass = false;
    boolean CanPlayerPass = false;
    int Health = 0;
}
