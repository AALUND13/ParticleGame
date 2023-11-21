package net.aalund13.particlegame.particle;

import net.aalund13.particlegame.util.ParticleObject;

import java.awt.*;

public class Water extends ParticleObject {
    public Water() {
        super(2, "Water", new Color(19, 104, 194), 1, particleType.WATER);
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
