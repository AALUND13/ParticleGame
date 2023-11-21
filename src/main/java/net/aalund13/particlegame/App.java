package net.aalund13.particlegame;

import javax.swing.*;

public class App {
    public static JFrame frame;

    public static void main(String[] args) throws Exception {
        int boardWidth = (int) (1000*1.4f);
        int boardHeight = 1000;

        frame = new JFrame("Particle Game");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            ParticleGame powderGame = new ParticleGame(boardWidth, boardHeight);
            frame.add(powderGame);
            frame.pack();
            frame.setVisible(true);
            powderGame.requestFocus();
        } catch (ParticleRegister.ParticleObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
