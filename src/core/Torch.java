package core;

import java.util.ArrayList;

public class Torch {
    public static ArrayList<ArrayList<Integer>> torches = new ArrayList<>();
    public ArrayList<Integer> position;

    public Torch(ArrayList<Integer> position) {
        this.position = position;
        torches.add(position);
    }


}
