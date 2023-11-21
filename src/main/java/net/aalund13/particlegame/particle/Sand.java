package net.aalund13.particlegame.particle;

import net.aalund13.particlegame.util.ParticleObject;

import java.awt.*;

public class Sand extends ParticleObject {
    public Sand() {
        super(1, "Sand", new Color(245, 212, 66), 2, particleType.POWDER);
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
