package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Room {
    static ArrayList<Room> rooms = new ArrayList<>();


    //BOTTOM LEFT
    public int x;
    public int y;


    public int width;
    public int height;
    public HashMap<String, Path> activePaths;

    public Room(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;


        this.width = w;
        this.height = h;
        this.activePaths = new HashMap<>();
    }

    /// non corner points one tile outside perimeter of floor
    public static boolean inBounds(int x, int y, int w, int h) {
        return x >= 0 && x < w && y < h && y >= 0;

    }

    public int[] getRandomTopPoint(Random r) {
        int rX = x + 1 + r.nextInt(width - 1);
        int rY = y + height + 1;
        return new int[]{rX, rY};
    }

    public int[] getRandomBottomPoint(Random r) {
        int rX = x + 1 + r.nextInt(width - 1);
        int rY = y - 1;
        return new int[]{rX, rY};
    }

    public int[] getRandomRightPoint(Random r) {
        int rX = x + width + 1;
        int rY = y + 1 + r.nextInt(height - 1);
        return new int[]{rX, rY};
    }

    public int[] getRandomLeftPoint(Random r) {
        int rX = x - 1;
        int rY = y + 1 + r.nextInt(height - 1);
        return new int[]{rX, rY};
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
