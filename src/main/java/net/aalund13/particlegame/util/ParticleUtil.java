package net.aalund13.particlegame.util;

import net.aalund13.particlegame.ParticleGame;
import net.aalund13.particlegame.ParticleRegister;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParticleUtil {
    public static class Tile {
        public ParticleObject powderObject;
        public int x;
        public int y;

        Tile(ParticleObject powderObject, int x, int y) {
            this.powderObject = powderObject;
            this.x = x;
            this.y = y;
        }
    }

    static public ArrayList<Tile> tiles = new ArrayList<>();
    static public int tileSize = 10;

    public static void setupTiles() throws ParticleRegister.ParticleObjectNotFoundException {
        for (int y = 0; y < ParticleGame.boardHeight / tileSize; y++) {
            for (int x = 0; x < ParticleGame.boardWidth / tileSize; x++) {
                tiles.add(new Tile(ParticleRegister.findParticleObjectById(0), x, y));
            }
        }
    }


    public static void moveTileAndUpdate(Tile tile, int x, int y) throws ParticleRegister.ParticleObjectNotFoundException {
        tile.powderObject.moveToX = tile.x + x;
        tile.powderObject.moveToY = tile.y + y;
        tile.powderObject.UpdateBeforeMove(tile.x, tile.y);
        moveTilesToPos(tile, tile.x + x, tile.y + y).powderObject.UpdateAfterMove(tile.x + x, tile.y + y);
    }

    public static void moveTile(Tile tile) throws ParticleRegister.ParticleObjectNotFoundException {
        if (tile.powderObject.powderType == ParticleObject.particleType.POWDER) {
            if (!isOutBound(tile.x, tile.y - 1)) {
                moveTileAndUpdate(tile,0, -1);
            }

            int RandomDir = ParticleGame.random.nextInt(0, 3);
            switch (RandomDir) {
                case 1:
                    if (!isOutBound(tile.x - 1, tile.y - 1))
                        moveTileAndUpdate(tile, -1, -1);
                    break;
                case 2:
                    if (!isOutBound(tile.x + 1, tile.y -1))
                        moveTileAndUpdate(tile, 1, -1);
                    break;
            }
        } else if (tile.powderObject.powderType == ParticleObject.particleType.WATER) {
            if (!isOutBound(tile.x, tile.y - 1)) {
                moveTileAndUpdate(tile,0, -1);
            }
            int RandomDir = ParticleGame.random.nextInt(0, 3);
            switch (RandomDir) {
                case 1:
                    if (!isOutBound(tile.x - 1, tile.y))
                        moveTileAndUpdate(tile, -1, 0);
                    break;
                case 2:
                    if (!isOutBound(tile.x + 1, tile.y))
                        moveTileAndUpdate(tile, 1, 0);
                    break;
            }
        }
    }

    public static void drawTileWithMouse() throws ParticleRegister.ParticleObjectNotFoundException {
        if (ParticleGame.mouseOnScreen) {
            int mouseXGrid = (ParticleGame.mouseX / tileSize);
            int mouseYGrid = (ParticleGame.boardHeight - ParticleGame.mouseY) / ParticleUtil.tileSize;

            if (ParticleGame.mouseBeingHold) {
                for (int i = 0; i < ParticleGame.brushSize; i++) {
                    for (int j = 0; j < ParticleGame.brushSize; j++) {
                        int x = mouseXGrid - (ParticleGame.brushSize - 1) / 2 + i;
                        int y = mouseYGrid - (ParticleGame.brushSize - 1) / 2 + j;
                        if (ParticleGame.spawnParticleWithMouse != 0 && !ParticleGame.rightClick) {
                            if (ParticleGame.holdingShift) {
                                replaceTile(ParticleGame.spawnParticleWithMouse, x, y);
                            } else {
                                placeTile(ParticleGame.spawnParticleWithMouse, x, y);
                            }
                        }else if (ParticleGame.spawnParticleWithMouse != 0) {
                            replaceTile(0, x, y);
                        }
                    }
                }
            }
        }
    }
    public static Tile moveTilesToPos(Tile tile, int x, int y) throws ParticleRegister.ParticleObjectNotFoundException {
        Tile tileAtPos = getTileAtPos(x, y);

        if (tileAtPos.powderObject.id == 0 || tile.powderObject.mass >= tileAtPos.powderObject.mass) {
            swapParticle(tile, tileAtPos.x, tileAtPos.y);
            return getTileAtPos(tileAtPos.x, tileAtPos.y);
        }

        return tile;
    }

    public static void swapParticle(Tile tile, int x, int y) throws ParticleRegister.ParticleObjectNotFoundException {
        Tile powderToSwapWith = getTileAtPos(x, y);

        ParticleObject temp = tile.powderObject;
        tile.powderObject = powderToSwapWith.powderObject;
        powderToSwapWith.powderObject = temp;
    }

    public static ArrayList<Tile> getNonAirNeighbor(int x, int y, int radius) {
        ArrayList<Tile> neighbor = getNeighbor(x, y, 1);
        return neighbor.stream()
                .filter(tile -> tile.powderObject.id != 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Tile> getNeighbor(int x, int y, int radius) {
        ArrayList<Tile> neighbor = new ArrayList<>();
        for (int x2 = x - radius; x2 <= x + radius; x2++) {
            for (int y2 = y - radius; y2 <= y + radius; y2++) {
                if (!isOutBound(x2, y2)) {
                    neighbor.add(getTileAtPos(x2, y2));
                }
            }
        }
        return neighbor;
    }

    public static boolean isOutBound(int x, int y) {
        return x * tileSize < 0 || x * tileSize >= ParticleGame.boardWidth || y * tileSize < 0 || y * tileSize >= ParticleGame.boardHeight;
    }

    public static Tile getTileAtPos(int x, int y) {
        // Calculate the index in the tiles list
        int index = (int) (MathUtil.clamp(y, 0, ParticleGame.boardHeight / tileSize - 1) * ParticleGame.boardWidth / tileSize + MathUtil.clamp(x, 0, ParticleGame.boardWidth / tileSize - 1));

        // Ensure the index is within bounds
        index = (int) MathUtil.clamp(index, 0, ParticleUtil.tiles.size() - 1);

        // Return the corresponding tile
        return ParticleUtil.tiles.get(index);
    }



    public static Tile getTileAtPos(int x, int y, ArrayList<Tile> tiles) {
        int clampedX = (int) MathUtil.clamp(x, 0, ParticleGame.boardWidth / tileSize - 1);
        int clampedY = (int) MathUtil.clamp(y, 0, ParticleGame.boardHeight / tileSize - 1);

        return tiles.stream()
                .filter(tile -> tile.x == clampedX && tile.y == clampedY)
                .findFirst()
                .orElse(null); // Handle the case when no tile is found at the specified position
    }

    public static void placeTile(int id, int x, int y) throws ParticleRegister.ParticleObjectNotFoundException {
        placeTile(id, x, y, true);
    }

    public static void placeTile(int id, int x, int y, boolean runSpawnMethod) throws ParticleRegister.ParticleObjectNotFoundException {
        Tile tile = getTileAtPos(x, y);
        if (tile.powderObject.id == 0) {
            tile.powderObject = ParticleRegister.findParticleObjectById(id);
            if (runSpawnMethod) tile.powderObject.Spawn(x, y);
        }
    }

    public static void replaceTile(int id, int x, int y) throws ParticleRegister.ParticleObjectNotFoundException {
        replaceTile(id, x, y, true);
    }
    public static void replaceTile(int id, int x, int y, boolean runSpawnMethod) throws ParticleRegister.ParticleObjectNotFoundException {
        Tile tile = getTileAtPos(x, y);
        tile.powderObject = ParticleRegister.findParticleObjectById(id);
        if (runSpawnMethod) tile.powderObject.Spawn(x, y);
    }
}
