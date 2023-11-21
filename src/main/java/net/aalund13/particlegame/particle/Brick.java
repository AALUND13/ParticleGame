package net.aalund13.particlegame.particle;

import net.aalund13.particlegame.util.ParticleObject;

import java.awt.*;

public class Brick extends ParticleObject {
    public Brick() {
        super(3, "Brick", new Color(245, 99, 66), 10, particleType.SOLID);
    }

    @Override
    public void Spawn(int x, int y) {

    }

    @Override
    public void UpdateAfterMove(int x, int y) {

    }

    @Override
    public void UpdateBeforeMove(int x, int y) {

    }
}
