package net.aalund13.particlegame.util;

import java.awt.*;

public abstract class ParticleObject {
    public enum particleType {
        SOLID,
        POWDER,
        WATER,
        GAS
    }

    public Color color = Color.gray;
    public particleType powderType = particleType.SOLID;

    public int id = 0; // ID 0 is 'Air'
    public String name = "Air";

    public int moveToX = 0;
    public int moveToY = 0;

    public int mass = 0;

    public abstract void Spawn(int x, int y);
    public abstract void UpdateAfterMove(int x, int y);
    public abstract void UpdateBeforeMove(int x, int y);

    public ParticleObject(int ID, String name, Color color, int mass, particleType powderType) {
        this.id = ID;
        this.name = name;
        this.color = color;
        this.mass = mass;
        this.powderType = powderType;
    }
}
