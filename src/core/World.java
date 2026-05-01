package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class World {
    public TETile[][] tiles;
    public Random r;
    public Map<List<Integer>, TETile> map;
    public int worldHeight;
    public int worldWidth;
    public TERenderer t;
    public boolean isNew;
    public int avatarX;
    public int avatarY;
    public String placeD;
    public boolean placeB = false;
    public int saveSlot = 1;
    public boolean command = false;
    public long s;

    public World(int w, int h, long s, boolean isNew, int avatarX, int avatarY, int saveSlot) {
        r = new Random(s);
        this.s = s;
        t = new TERenderer();
        t.initialize(w, h);
        this.worldWidth = w;
        this.worldHeight = h;
        this.isNew = isNew;
        this.avatarX = avatarX;
        this.avatarY = avatarY;
        this.saveSlot = saveSlot;
        tiles = new TETile[w][h - 2];
        map = new HashMap<>();
        h = h - 2;
        this.worldHeight = h;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                tiles[x][y] = Tileset.NOTHING;
                map.put(new ArrayList<>(Arrays.asList(x, y)), Tileset.NOTHING);
            }
        }

        int steps = r.nextInt(1) + 5;

        //ArrayList<ArrayList<Integer>> rooms = new ArrayList<>();
        int z = 0;
        while (z < steps) {
            int x = r.nextInt(w - 1) + 1;
            int y = r.nextInt(h - 1) + 1;

            while (map.get(Arrays.asList(x, y)) != Tileset.NOTHING) {
                x = r.nextInt(w);
                y = r.nextInt(h);
            }
            int length = r.nextInt(7) + 4;
            int width = r.nextInt(7) + 4;
            if (x + length + 1 >= w || y + width + 1 >= h) {
                continue;
            }
            boolean stop = false;
            for (int i = x - 1; i <= x + length + 1; i++) {
                for (int j = y - 1; j <= y + width + 1; j++) {
                    if (map.get(Arrays.asList(i, j)) == Tileset.FLOOR || map.get(Arrays.asList(i, j)) == Tileset.WALL) {
                        stop = true;
                        break;
                    }
                }
                if (stop) {
                    break;
                }
            }
            if (stop) {
                continue;
            }
            //rooms.add(new ArrayList<Integer>(Arrays.asList(x,y,length,width)));

            Room.rooms.add(new Room(x, y, length, width));


            for (int i = x; i <= x + length; i++) {
                for (int j = y; j <= y + width; j++) {
                    if (i == x || i == x + length || j == y || j == y + width) {
                        tiles[i][j] = Tileset.WALL;
                        map.put(Arrays.asList(i, j), Tileset.WALL);
                    }

                    map.put(Arrays.asList(i, j), Tileset.FLOOR);
                    tiles[i][j] = Tileset.FLOOR;
                }
            }
            z++;
        }
//        for(Room room: Room.rooms) {
//            int[] testing = room.getRandomRightPoint(r);
//            if (Room.inBounds(testing[0], testing[1], w, h)) {
//                tiles[testing[0]][testing[1]] = Tileset.WALL;
//            }
//            testing = room.getRandomLeftPoint(r);
//            if (Room.inBounds(testing[0], testing[1], w, h)) {
//                tiles[testing[0]][testing[1]] = Tileset.WALL;
//            }
//            testing = room.getRandomBottomPoint(r);
//            if (Room.inBounds(testing[0], testing[1], w, h)) {
//                tiles[testing[0]][testing[1]] = Tileset.WALL;
//            }
//            testing = room.getRandomTopPoint(r);
//            if (Room.inBounds(testing[0], testing[1], w, h)) {
//                tiles[testing[0]][testing[1]] = Tileset.WALL;
//            }
//        }

        this.generatePaths(r);
        this.makeWalls(r);
        avatarStartingPoint();
        torchesPoints();


        StdDraw.clear(new Color(0, 0, 0));
        t.drawTiles(tiles);
        StdDraw.setPenColor(Color.DARK_GRAY);
        StdDraw.filledRectangle(worldWidth / 2, h + 1, worldWidth / 2, 1);
        StdDraw.show();

        while (true) {
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();


            drawLight();

            for (ArrayList<Integer> torch : Torch.torches) {
                map.put(new ArrayList(Arrays.asList(torch.getFirst(), torch.getLast())), Tileset.TORCH);
                tiles[torch.getFirst()][torch.getLast()] = Tileset.TORCH;
            }


            TETile place = map.get(new ArrayList<>(Arrays.asList((int) Math.round(x), (int) Math.round(y) - 1)));
            if (place != null) {
                if (StdDraw.isMousePressed() && place.equals(Tileset.FLOOR)) {
                    ArrayList<ArrayList<Integer>> start = new ArrayList<>();
                    start.add(Avatar.players.get(0).position);

                    ArrayList<ArrayList<Integer>> path = findPath(Avatar.players.get(0).position, new ArrayList<>(Arrays.asList((int) Math.round(x), (int) Math.round(y) - 1)), start);
                    for (int i = 0; i < path.size() - 1; i++) {
                        tiles[path.get(i).getFirst()][path.get(i).getLast()] = Tileset.WATER;
                        map.put(path.get(i), Tileset.WATER);

                    }
                    //10
                    StdDraw.pause(10);

                    StdDraw.clear(new Color(0, 0, 0));
                    StdDraw.setPenRadius(0.05);
                    StdDraw.setPenColor(Color.DARK_GRAY);
                    StdDraw.filledRectangle(worldWidth / 2, h + 1, worldWidth / 2, 1);
                    if (placeB) {
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.textLeft(2, h + 1, placeD);
                    }
                    t.drawTiles(tiles);
                    StdDraw.show();
                    t.drawTiles(tiles);
                    StdDraw.show();
                    for (int i = path.size() - 2; i >= 0; i--) {
                        //472
                        StdDraw.pause(472);


                        tiles[path.get(i + 1).getFirst()][path.get(i + 1).getLast()] = Tileset.FLOOR;
                        tiles[path.get(i).getFirst()][path.get(i).getLast()] = Tileset.AVATAR;


                        map.put(path.get(i), Tileset.AVATAR);
                        map.put(path.get(i + 1), Tileset.FLOOR);

                        drawLight();


                        Tileset.AVATAR.backgroundColor = tiles[path.get(i).getFirst()][path.get(i).getLast()].backgroundColor;


                        StdDraw.clear(new Color(0, 0, 0));
                        t.drawTiles(tiles);


                        StdDraw.setPenRadius(0.05);
                        StdDraw.setPenColor(Color.DARK_GRAY);
                        StdDraw.filledRectangle(worldWidth / 2, h + 1, worldWidth / 2, 1);
                        if (placeB) {
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.textLeft(2, h + 1, placeD);
                        }


                        StdDraw.show();
                    }
                    Avatar.players.getFirst().position = path.getFirst();
                    map.put(path.getLast(), Tileset.FLOOR);
                    map.put(path.getFirst(), Tileset.AVATAR);
                }
                Font font = new Font("Arial", Font.TRUETYPE_FONT, 10);
                StdDraw.setFont(font);
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(Color.DARK_GRAY);
                StdDraw.filledRectangle(worldWidth / 2, h + 1, worldWidth / 2, 1);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, h + 1, place.description());
                placeD = place.description();
                placeB = true;

                StdDraw.show();
            }

            if (StdDraw.hasNextKeyTyped()) {
                String i = String.valueOf(StdDraw.nextKeyTyped());


                if (command) {
                    if ((i.equals("q") || i.equals("Q"))) {

                        String filename = "save.txt";
                        String[] lines = new String[9];


                        for (int j = 0; j < 9; j++) {
                            lines[j] = "0";
                        }


                        File file = new File(filename);
                        if (file.exists()) {
                            In in = new In(file);
                            for (int j = 0; j < 9 && in.hasNextLine(); j++) {
                                lines[j] = in.readLine();
                            }
                        }

                        int start = (saveSlot - 1) * 3;
                        lines[start] = String.valueOf(s);

                        lines[start + 1] = String.valueOf(Avatar.players.get(0).position.get(0));
                        lines[start + 2] = String.valueOf(Avatar.players.get(0).position.get(1));


                        Out out = new Out(filename);
                        for (int j = 0; j < 9; j++) {
                            out.println(lines[j]);
                        }
                        out.close();

                        System.out.println(Room.rooms);
                        System.out.println(Torch.torches);

                        System.exit(0);


                    }
                    command = false;
                } else {

                    if (i.equals(":")) {
                        command = true;
                    }


                    if (i.equals("1")) {
                        saveSlot = 1;
                    }

                    if (i.equals("2")) {
                        saveSlot = 2;
                    }

                    if (i.equals("3")) {
                        saveSlot = 3;
                    }

                    if (i.equals("w") || i.equals("W")) {
                        ArrayList<Integer> up = new ArrayList<>(Arrays.asList(Avatar.players.get(0).position.getFirst(), Avatar.players.get(0).position.getLast() + 1));
                        if (map.get(up).equals(Tileset.FLOOR)) {

                            //torchesPoints();
                            tiles[up.getFirst()][up.getLast()] = Tileset.AVATAR;
                            tiles[up.getFirst()][up.getLast() - 1] = Tileset.FLOOR;
                            map.put(up, Tileset.AVATAR);
                            Avatar.players.get(0).position = up;
                            map.put(new ArrayList<>(Arrays.asList(up.getFirst(), up.getLast() - 1)), Tileset.FLOOR);


                        }
                    }
                    if (i.equals("d") || i.equals("D")) {
                        ArrayList<Integer> up = new ArrayList<>(Arrays.asList(Avatar.players.get(0).position.getFirst() + 1, Avatar.players.get(0).position.getLast()));
                        if (map.get(up).equals(Tileset.FLOOR)) {

                            //torchesPoints();

                            tiles[up.getFirst()][up.getLast()] = Tileset.AVATAR;
                            tiles[up.getFirst() - 1][up.getLast()] = Tileset.FLOOR;
                            map.put(up, Tileset.AVATAR);
                            Avatar.players.get(0).position = up;
                            map.put(new ArrayList<>(Arrays.asList(up.getFirst() - 1, up.getLast())), Tileset.FLOOR);


                        }
                    }
                    if (i.equals("S") || i.equals("s")) {
                        ArrayList<Integer> up = new ArrayList<>(Arrays.asList(Avatar.players.get(0).position.getFirst(), Avatar.players.get(0).position.getLast() - 1));
                        if (map.get(up).equals(Tileset.FLOOR)) {
                            if (map.get(up).equals(Tileset.TORCH)) {
                                //System.out.println('a');
                            }

                            //torchesPoints();
                            tiles[up.getFirst()][up.getLast()] = Tileset.AVATAR;
                            tiles[up.getFirst()][up.getLast() + 1] = Tileset.FLOOR;
                            map.put(up, Tileset.AVATAR);
                            Avatar.players.get(0).position = up;


                            map.put(new ArrayList<>(Arrays.asList(up.getFirst(), up.getLast() + 1)), Tileset.FLOOR);


                        }
                    }
                    if (i.equals("a") || i.equals("A")) {
                        ArrayList<Integer> up = new ArrayList<>(Arrays.asList(Avatar.players.get(0).position.getFirst() - 1, Avatar.players.get(0).position.getLast()));
                        if (map.get(up).equals(Tileset.FLOOR)) {
                            //torchesPoints();
                            tiles[up.getFirst()][up.getLast()] = Tileset.AVATAR;
                            tiles[up.getFirst() + 1][up.getLast()] = Tileset.FLOOR;
                            map.put(up, Tileset.AVATAR);
                            Avatar.players.get(0).position = up;

                            map.put(new ArrayList<>(Arrays.asList(up.getFirst() + 1, up.getLast())), Tileset.FLOOR);


                        }
                    }

                }


            }

            StdDraw.pause(20);
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.setPenRadius(0.05);
            for (int l = 0; l < tiles.length; l++) {
                for (int m = 0; m < tiles[l].length; m++) {
                    if (map.get(new ArrayList<>(Arrays.asList(l, m))).description.equals("torch") && (new ArrayList<>(Arrays.asList(l, m))).equals(Avatar.players.getFirst().position)) {
                        tiles[l][m] = Tileset.AVATAR;
                        map.put(new ArrayList<>(Arrays.asList(l, m)), Tileset.AVATAR);
                    }
                }
            }
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.filledRectangle(worldWidth / 2, h + 1, worldWidth / 2, 1);
            if (placeB) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, h + 1, placeD);
            }
            t.drawTiles(tiles);
            StdDraw.show();
        }
    }
    //____END OF CONSTRUCTOR

    public ArrayList<ArrayList<Integer>> findPath(ArrayList<Integer> start, ArrayList<Integer> end, ArrayList<ArrayList<Integer>> path) {

        Queue<ArrayList<Integer>> candidates = new LinkedList<>();
        HashSet<ArrayList<Integer>> visited = new HashSet<>();
        HashMap<ArrayList<Integer>, ArrayList<Integer>> parents = new HashMap<>();

        candidates.add(start);
        parents.put(start, null);
        visited.add(start);

        while (!candidates.isEmpty()) {
            ArrayList<Integer> current = candidates.remove();


            if (current.equals(end)) {
                ArrayList<Integer> parent = parents.get(current);
                ArrayList<ArrayList<Integer>> bath = new ArrayList<>();
                bath.add(current);
                while (parent != null) {
                    bath.add(parent);
                    parent = parents.get(parent);

                }
                return bath;
            }

            ArrayList<Integer> up = new ArrayList<>(Arrays.asList(current.getFirst(), current.getLast() + 1));
            if (map.get(up).equals(Tileset.FLOOR) && !visited.contains(up)) {
                candidates.add(up);
                visited.add(up);
                parents.put(up, current);
            }

            ArrayList<Integer> down = new ArrayList<>(Arrays.asList(current.getFirst(), current.getLast() - 1));
            if (map.get(down).equals(Tileset.FLOOR) && !visited.contains(down)) {
                candidates.add(down);
                visited.add(down);
                parents.put(down, current);

            }

            ArrayList<Integer> left = new ArrayList<>(Arrays.asList(current.getFirst() - 1, current.getLast()));
            if (map.get(left).equals(Tileset.FLOOR) && !visited.contains(left)) {

                candidates.add(left);
                visited.add(left);
                parents.put(left, current);
            }
            ArrayList<Integer> right = new ArrayList<>(Arrays.asList(current.getFirst() + 1, current.getLast()));
            if (map.get(right).equals(Tileset.FLOOR) && !visited.contains(right)) {
                candidates.add(right);
                parents.put(right, current);
                visited.add(right);
            }
        }
        return new ArrayList<ArrayList<Integer>>();
    }


    public void torchesPoints() {


        for (Room room : Room.rooms) {

            Random r = new Random(s + room.getX() + room.getY() * 1000L);
            int rX = r.nextInt(room.getWidth()) + room.getX();
            int rY = r.nextInt(room.getHeight()) + room.getY();

            while (map.get(new ArrayList<>(Arrays.asList(rX, rY))) != Tileset.FLOOR) {
                rX = r.nextInt(room.getWidth()) + room.getX();
                rY = r.nextInt(room.getHeight()) + room.getY();

            }
            ArrayList<Integer> position = new ArrayList<>(Arrays.asList(rX, rY));

            Torch touch = new Torch(position);
            map.put(position, Tileset.TORCH);
            tiles[rX][rY] = Tileset.TORCH;

        }
    }

    public void drawLight() {
        for (ArrayList<Integer> start : Torch.torches) {
            int lX = start.getFirst() - 4;
            int lY = start.getLast() - 4;
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    ArrayList<Integer> pot = new ArrayList<>(Arrays.asList(lX + x, lY + y));

                    if (Room.inBounds(pot.getFirst(), pot.getLast(), this.worldWidth, this.worldHeight) && map.get(pot).description.equals("floor") && (x != 4 || y != 4) && checkValidLight(start.getFirst(), start.getLast(), pot.getFirst(), pot.getLast())) {
                        int depth = Math.max(Math.abs(x - 4), Math.abs(y - 4));


//                            System.out.println((float) (1 - (0.2 * (float) depth)));
//                            System.out.println(map.get(pot).backgroundColor.getAlpha());


                        if ((int) ((1 - (0.2 * (float) depth)) * 255) < map.get(pot).backgroundColor.getAlpha() && !(map.get(pot).backgroundColor.equals(Color.BLACK))) {
                            continue;

                        }

                        tiles[pot.getFirst()][pot.getLast()] = new TETile('·', Color.WHITE, new Color((float) 149 / 255, (float) 51 / 255, (float) 240 / 255, (float) (1 - (0.2 * (float) depth))), "floor", 2);
                        map.put(pot, new TETile('·', Color.WHITE, new Color((float) 149 / 255, (float) 51 / 255, (float) 240 / 255, (float) (1 - ((.2 * depth)))), "floor", 2));


                    }
                }


            }


        }
    }


    public boolean checkValidLight(int torchX, int torchY, int lightX, int lightY) {
        int xDistance = Math.abs(torchX - lightX);
        int yDistance = Math.abs(torchY - lightY);

        int xDirection = 1;
        int yDirection = 1;

        if (torchX > lightX) {
            xDirection = -1;
        }

        if (torchY > lightY) {
            yDirection = -1;
        }

        int error = xDistance - yDistance;

        int x = torchX;
        int y = torchY;


        while (!(x == lightX && y == lightY)) {
            TETile tile = map.get(new ArrayList<>(Arrays.asList(x, y)));

            if (tile == null || tile.equals(Tileset.WALL) || tile.equals(Tileset.NOTHING)) {
                return false;
            }

            int test = Tileset.WALL.backgroundColor.getAlpha();

            int error2 = 2 * error;

            if (error2 > -yDistance) {
                error -= yDistance;
                x += xDirection;
            }

            if (error2 < xDistance) {
                error += xDistance;
                y += yDirection;
            }


        }
        return true;
    }


    public void avatarStartingPoint() {
        if (isNew) {

            for (TETile[] x : tiles) {
                for (TETile y : x) {
                    int Rx = r.nextInt(worldWidth);
                    int Ry = r.nextInt(worldHeight);

                    if (map.get(new ArrayList<>(Arrays.asList(Rx, Ry))) == Tileset.FLOOR) {
                        Avatar.players.add(new Avatar(new ArrayList<Integer>(Arrays.asList(Rx, Ry))));
                        map.put(new ArrayList<>(Arrays.asList(Rx, Ry)), Tileset.AVATAR);
                        tiles[Rx][Ry] = Tileset.AVATAR;
                        return;
                    }


                }
            }

        } else {

            Avatar.players.add(new Avatar(new ArrayList<Integer>(Arrays.asList(avatarX, avatarY))));
            map.put(new ArrayList<>(Arrays.asList(avatarX, avatarY)), Tileset.AVATAR);
            tiles[avatarX][avatarY] = Tileset.AVATAR;

        }
    }


    public void makeWalls(Random r) {
        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                if (map.get(new ArrayList<>(Arrays.asList(x, y))).equals(Tileset.FLOOR)) {
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if ((Room.inBounds(x + i, y + j, worldWidth, worldHeight)) && map.get(new ArrayList<>(Arrays.asList(x + i, y + j))).equals(Tileset.NOTHING)) {
                                map.put(new ArrayList<>(Arrays.asList(x + i, y + j)), Tileset.WALL);
                                tiles[x + i][y + j] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }

    public void generatePaths(Random r) {
        for (Room room : Room.rooms) {
            int[] testing = room.getRandomRightPoint(r);
            if (Room.inBounds(testing[0], testing[1], worldWidth, worldHeight)) {
                tiles[testing[0]][testing[1]] = Tileset.FLOOR;
                map.put(new ArrayList<>(Arrays.asList(testing[0], testing[1])), Tileset.FLOOR);
                room.activePaths.put("right", new Path(testing[0], testing[1], 1));
                Path.AllActivePaths.add(room.activePaths.get("right"));

            }

            testing = room.getRandomLeftPoint(r);
            if (Room.inBounds(testing[0], testing[1], worldWidth, worldHeight)) {
                tiles[testing[0]][testing[1]] = Tileset.FLOOR;
                map.put(new ArrayList<>(Arrays.asList(testing[0], testing[1])), Tileset.FLOOR);
                room.activePaths.put("left", new Path(testing[0], testing[1], 3));
                Path.AllActivePaths.add(room.activePaths.get("left"));

            }
            testing = room.getRandomBottomPoint(r);
            if (Room.inBounds(testing[0], testing[1], worldWidth, worldHeight)) {
                tiles[testing[0]][testing[1]] = Tileset.FLOOR;
                map.put(new ArrayList<>(Arrays.asList(testing[0], testing[1])), Tileset.FLOOR);
                room.activePaths.put("bottom", new Path(testing[0], testing[1], 2));
                Path.AllActivePaths.add(room.activePaths.get("bottom"));

            }
            testing = room.getRandomTopPoint(r);
            if (Room.inBounds(testing[0], testing[1], worldWidth, worldHeight)) {
                tiles[testing[0]][testing[1]] = Tileset.FLOOR;
                map.put(new ArrayList<>(Arrays.asList(testing[0], testing[1])), Tileset.FLOOR);
                room.activePaths.put("top", new Path(testing[0], testing[1], 0));
                Path.AllActivePaths.add(room.activePaths.get("top"));

            }

        }

        for (int i = 0; i < Math.max(worldWidth, worldHeight); i++) {
            for (Path p : Path.AllActivePaths) {
                if (!p.completed && !p.isHalted) {
                    ArrayList<Integer> test = p.add();

                    if (Room.inBounds(test.get(0), test.get(1), worldWidth, worldHeight)) {
                        Path a = new Path(5, 4, 1);
                        Path b = new Path(4, 2, 3);

                        if (map.get(p.path.getLast()).equals(Tileset.FLOOR)) {
                            p.completed = true;
                            for (Path pa : Path.AllActivePaths) {
                                ArrayList<Integer> c = p.path.getLast();

                                if (pa.pHash.contains(c)) {
                                    if (!p.equals(pa)) {
                                        pa.intersections.add(c);
                                        p.intersections.add(c);
                                        pa.noSave = false;
                                        p.noSave = false;
                                        pa.completed = true;
                                        pa.isHalted = true;
                                    }


                                }
                            }

                        }
                        tiles[test.get(0)][test.get(1)] = Tileset.FLOOR;
                        map.put(p.path.getLast(), Tileset.FLOOR);
                    } else {
                        p.isHalted = true;
                        if (p.intersections.isEmpty()) {
                            p.noSave = true;
                        }
                        p.path.removeLast();
                    }
                }
            }

        }


        for (Path p : Path.AllActivePaths) {
            if (p.isHalted && p.noSave) {
                for (ArrayList<Integer> c : p.path) {
                    tiles[c.getFirst()][c.getLast()] = Tileset.NOTHING;
                    map.put(new ArrayList<>(Arrays.asList(c.getFirst(), c.getLast())), Tileset.NOTHING);
                }
            } else if (p.isHalted && !p.noSave) {
                int size = p.path.size();
                for (int i = 0; i < size; i++) {
                    ArrayList<Integer> c = p.path.removeLast();
                    ArrayList<Integer> t = new ArrayList<>(Arrays.asList(c.getFirst(), c.getLast()));
                    if (p.intersections.contains(t)) {
                        break;
                    }
                    tiles[c.getFirst()][c.getLast()] = Tileset.NOTHING;
                    map.put(new ArrayList<>(Arrays.asList(c.getFirst(), c.getLast())), Tileset.NOTHING);
                }
            }


        }
    }
}
