package net.aalund13.particlegame;

import net.aalund13.particlegame.particle.Brick;
import net.aalund13.particlegame.particle.Water;
import net.aalund13.particlegame.util.ParticleObject;
import net.aalund13.particlegame.particle.Air;
import net.aalund13.particlegame.particle.Sand;

import java.util.ArrayList;

public class ParticleRegister {
    public static ArrayList<ParticleObject> registerObject = new ArrayList<>();

    public static void RegisterAllObject() {
        RegisterParticleObject(new Air());
        RegisterParticleObject(new Sand());
        RegisterParticleObject(new Water());
        RegisterParticleObject(new Brick());
    }

    public static void RegisterParticleObject(ParticleObject powderObject) {
        registerObject.add(powderObject);
        System.out.println("Particle: " + powderObject.name + ", ID: " + powderObject.id);
    }

    public static ParticleObject findParticleObjectById(int id) throws ParticleObjectNotFoundException {
        for (int i = 0; i < registerObject.size(); i++) {
            ParticleObject powderObject = registerObject.get(i);
            if (powderObject.id == id) {
                return powderObject;
            }
        }
        throw new ParticleObjectNotFoundException("Unknown Particle ID: " + id);

    }

    // Custom exception class
    public static class ParticleObjectNotFoundException extends Exception {
        public ParticleObjectNotFoundException(String message) {
            super(message);
        }
    }

}
