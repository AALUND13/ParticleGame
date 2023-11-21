package net.aalund13.particlegame;

import net.aalund13.particlegame.util.MathUtil;
import net.aalund13.particlegame.util.ParticleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.*;

public class ParticleGame extends JPanel implements ActionListener, KeyListener, MouseListener, MouseWheelListener {
    public static int framesPerSecond = 60;

    public static int boardWidth;
    public static int boardHeight;

    public static int mouseX = 0;
    public static int mouseY = 0;
    public static boolean mouseOnScreen = false;
    public static boolean mouseBeingHold = false;
    public static boolean rightClick = false;

    public static boolean holdingShift = false;

    public static int spawnParticleWithMouse = 0;

    int tileSize = ParticleUtil.tileSize;

    public static int brushSize = 1;

    Timer gameLoop;
    public static Random random;
    private int frameCount = 0;
    private long startTime;

    public ParticleGame(int boardWidth, int boardHeight) throws ParticleRegister.ParticleObjectNotFoundException {
        ParticleGame.boardWidth = boardWidth;
        ParticleGame.boardHeight = boardHeight;

        setPreferredSize(new Dimension(ParticleGame.boardWidth, ParticleGame.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        setFocusable(true);

        ParticleRegister.RegisterAllObject();
        ParticleUtil.setupTiles();

        startTime = System.currentTimeMillis();
        random = new Random();

        gameLoop = new Timer((int) (1.0 / framesPerSecond * 1000), this);
        gameLoop.start();

        int numThreads = 4; // Adjust the number of threads as needed
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(numThreads);
        scheduledExecutorService.scheduleAtFixedRate(this::moveTiles, 0, 1000 / framesPerSecond, TimeUnit.MILLISECONDS);
    }

    private void moveTiles() {
        // Check if the tiles list is not null and not empty
        for (int i = 0; i < boardWidth / tileSize * boardHeight / tileSize; i++) {
            ParticleUtil.Tile tile = ParticleUtil.tiles.get(i);
            if (tile.powderObject.id != 0) {
                try {
                    ParticleUtil.moveTile(tile);
                } catch (ParticleRegister.ParticleObjectNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Increment the frame count
        frameCount++;

        // Calculate elapsed time
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        // Update FPS every second
        if (elapsedTime >= 1000) {
            // Calculate and print FPS
            int fps = (int) (frameCount / (elapsedTime / 1000.0));
            System.out.println("FPS: " + fps);

            // Reset frame count and start time
            frameCount = 0;
            startTime = currentTime;
        }

        repaint();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize * boardHeight / tileSize; i++) {
            ParticleUtil.Tile tile = ParticleUtil.tiles.get(i);
            g.setColor(tile.powderObject.color);
            g.fillRect(tile.x * tileSize, (boardHeight / tileSize - 1 - tile.y) * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.darkGray);

        // Draw grid lines
        g.setColor(Color.darkGray);
        for (int i = 0; i <= boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
        }
        for (int i = 0; i <= boardHeight / tileSize; i++) {
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Mouse
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            int mouseXGrid = (mousePosition.x / tileSize);
            int mouseYGrid = (mousePosition.y / tileSize);
            int trueMouseYGrid = (boardHeight - mouseY) / ParticleUtil.tileSize;

            int mouseX = mouseXGrid * tileSize;
            int mouseY = mouseYGrid * tileSize;

            //Hover Tile Text
            int fontSize = 16;

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, fontSize));
            String text = "Particle: " + ParticleUtil.getTileAtPos(mouseXGrid, trueMouseYGrid).powderObject.name + " | Position: " + mouseXGrid + ", " + mouseYGrid;
            g.drawString(text, boardWidth - (text.length() * fontSize / 2) - 5, 25);

            g.setColor(Color.white);

            // Draw a rectangle around the rounded mouse cursor with the brush size
            int brushOffset = (brushSize - 1) * tileSize / 2;
            g.drawRect(mouseX - brushOffset, mouseY - brushOffset, brushSize * tileSize, brushSize * tileSize);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseBeingHold = true;
        if (e.getButton() == 3) {
            rightClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseBeingHold = false;
        rightClick = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            mouseX = mousePosition.x;
            mouseY = mousePosition.y;

            mouseOnScreen = true;
        }else {
            mouseOnScreen = false;
        }

        try {
            ParticleUtil.drawTileWithMouse();
        } catch (ParticleRegister.ParticleObjectNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key");
        char keyChar = e.getKeyChar();

        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            holdingShift = true;
        }

        if (Character.isDigit(keyChar)) {
            int digitValue = Character.getNumericValue(keyChar);

            // Assuming spawnParticleWithMouse should be set to the digit value
            spawnParticleWithMouse = (int) MathUtil.clamp(digitValue, 0, ParticleRegister.registerObject.size() - 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            holdingShift = false;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            // Scroll up, increase brush size
            brushSize = Math.min(brushSize + 2, 11); // Adjust the maximum brush size if needed
        } else {
            // Scroll down, decrease brush size
            brushSize = Math.max(brushSize - 2, 1); // Adjust the minimum brush size if needed
        }
    }
}
