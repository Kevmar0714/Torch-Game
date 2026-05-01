package core;

import java.util.ArrayList;

public class Avatar {
    public ArrayList<Integer> position;
    public static ArrayList<Avatar> players = new ArrayList<>();

    public Avatar(ArrayList<Integer> starting_point) {
        this.position = starting_point;

    }

}
