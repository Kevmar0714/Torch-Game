package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Path {
    public static ArrayList<Path> AllActivePaths = new ArrayList<>();
    public boolean completed = false;
    public ArrayList<ArrayList<Integer>> path = new ArrayList<>();
    ArrayList<ArrayList<Integer>> ends = new ArrayList<>();
    HashSet<ArrayList<Integer>> pHash = new HashSet<>();
    public boolean isHalted = false;
    public boolean noSave = false;

    public ArrayList<ArrayList<Integer>> intersections = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> hit = new ArrayList<>();
    int direction;

    // direction mappings: up:0 right:1 down:2 left:3
    public Path(int x, int y, int dir) {
        ArrayList<Integer> start = new ArrayList<>(Arrays.asList(x, y));
        ends.add(start);
        path.add(start);
        pHash.add(start);
        this.direction = dir;
    }
//    public void add(){
//        ArrayList<Integer> next = new ArrayList<>();
//        ArrayList<Integer> last = path.getLast();
//        switch (direction){
//            case 0:
//                next.add(last.get(0), last.get(1)-1);
//                break;
//            case 1:
//                next.add(last.get(0) + 1, last.get(1));
//                break;
//            case 2:
//                next.add(last.get(0), last.get(1)+1);
//            case 3:
//                next.add(last.get(0) -1, last.get(1));
//        }
//        path.add(next);
//        pHash.add(next);
//    }

    // direction mappings: up:0 right:1 down:2 left:3
    public ArrayList<Integer> add() {
        ArrayList<Integer> next = new ArrayList<>();
        ArrayList<Integer> last = path.getLast();
        switch (direction) {
            case 0:
                next.add(last.get(0));
                next.add(last.get(1) + 1);
                break;
            case 1:
                next.add(last.get(0) + 1);
                next.add(last.get(1));
                break;
            case 2:
                next.add(last.get(0));
                next.add(last.get(1) - 1);
                break;
            case 3:
                next.add(last.get(0) - 1);
                next.add(last.get(1));
                break;
        }

        path.add(next);
        pHash.add(next);
        return next;
    }

}
