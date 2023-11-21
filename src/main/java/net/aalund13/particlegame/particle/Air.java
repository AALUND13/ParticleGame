package net.aalund13.particlegame.particle;

import net.aalund13.particlegame.util.ParticleObject;

import java.awt.*;

public class Air extends ParticleObject {
    public Air() {
        super(0, "Air", new Color(0, 0, 0),0 , particleType.POWDER);
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
