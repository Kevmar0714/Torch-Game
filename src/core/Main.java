package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class Main {

    public int saveFile = 0;

    public static void main(String[] args) {

        //149857

        /// SEEED OF INTEreST OFF BY ONE PiXEL 1498019550, 12934775


        Font font = new Font("Arial", Font.BOLD, 80);
        StdDraw.setFont(font);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, .85, "Torch Game");
        Font font2 = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(font2);

        StdDraw.text(0.5, .60, "(N) New Game");
        StdDraw.text(0.5, .50, "(L) Load Game");
        StdDraw.text(0.5, 0.40, "(M) Save Slots");
        StdDraw.text(0.5, .30, "(Q) Quit Game");
        StdDraw.show();
        StdDraw.pause(10);

        int saveSlot = 1;

        while (true) {
            if (StdDraw.isKeyPressed(78)) {
                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(font);
                StdDraw.text(0.5, .85, "Torch Game");
                StdDraw.setFont(font2);
                StdDraw.text(0.5, .65, "Enter seed followed by S");
                Long s = (long) 0;
                StdDraw.nextKeyTyped();
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        String l = String.valueOf(StdDraw.nextKeyTyped());
                        System.out.println(l);
                        if (l.equals("s") || l.equals("S")) {
                            World world = new World(40, 40, s, true, 0, 0, saveSlot);
                            System.out.println('a');

                            break;
                        } else {
                            s *= 10;
                            s += Integer.parseInt(l);
                            StdDraw.setPenColor(Color.BLACK);
                            StdDraw.filledRectangle(0.5, 0.35, 1, .2);
                            StdDraw.setPenColor(Color.MAGENTA);
                            StdDraw.text(0.5, .45, String.valueOf(s));
                        }

                    }
                }

                break;
            } else if (StdDraw.isKeyPressed(76)) {


                String filename = "save.txt";
                File file = new File(filename);

                // Step 1: Check if the file exists and read the saved name
                if (file.exists()) {
                    In in = new In(file);
                    if (in.hasNextLine()) {

                        for (int i = 0; i < (((saveSlot - 1) * 3)); i++) {
                            String x = in.readLine();

                        }

                        String x = in.readLine();
                        long s = Long.parseLong(x);

                        x = in.readLine();
                        int avatarX = Integer.parseInt(x);

                        x = in.readLine();
                        int avatarY = Integer.parseInt(x);

                        World world = new World(40, 40, s, false, avatarX, avatarY, saveSlot);


                    } else {
                        System.out.println("I don't know your name.");
                    }
                } else {
                    System.out.println("I don't know your name.");
                }


                break;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_M)) {
                StdDraw.clear(Color.BLACK);

                StdDraw.setPenRadius(0.07);
                StdDraw.setPenColor(Color.WHITE);
                font2 = new Font("Arial", Font.BOLD, 40);
                StdDraw.setFont(font2);

                StdDraw.text(0.5, .60, "(1) Game 1");
                StdDraw.text(0.5, .50, "(2) Game 2");
                StdDraw.text(0.5, 0.40, "(3) Game 3");
                StdDraw.text(0.5, .30, "(E) Go Back");
                StdDraw.show();


                while (true) {

                    if (StdDraw.hasNextKeyTyped()) {
                        char key = StdDraw.nextKeyTyped();
                        if (key == 'e' || key == 'E') {

                            StdDraw.clear(Color.BLACK);

                            font = new Font("Arial", Font.BOLD, 80);
                            StdDraw.setFont(font);

                            StdDraw.clear(new Color(0, 0, 0));

                            StdDraw.setPenRadius(0.05);
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(0.5, .85, "Torch Game");
                            font2 = new Font("Arial", Font.BOLD, 40);
                            StdDraw.setFont(font2);

                            StdDraw.text(0.5, .60, "(N) New Game");
                            StdDraw.text(0.5, .50, "(L) Load Game");
                            StdDraw.text(0.5, 0.40, "(M) Save Slots");
                            StdDraw.text(0.5, .30, "(Q) Quit Game");
                            StdDraw.show();
                            StdDraw.pause(10);

                            break;
                        }

                        if (key == '1') {

                            saveSlot = 1;

                            StdDraw.clear(Color.BLACK);

                            StdDraw.setPenRadius(0.07);
                            StdDraw.setPenColor(Color.WHITE);
                            font2 = new Font("Arial", Font.BOLD, 40);
                            StdDraw.setFont(font2);

                            StdDraw.setPenColor(Color.GREEN);
                            StdDraw.text(0.5, .60, "(1) Game 1");
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(0.5, .50, "(2) Game 2");
                            StdDraw.text(0.5, 0.40, "(3) Game 3");
                            StdDraw.text(0.5, .30, "(E) Go Back");
                            StdDraw.show();


                        }

                        if (key == '2') {

                            saveSlot = 2;

                            StdDraw.clear(Color.BLACK);

                            StdDraw.setPenRadius(0.07);
                            StdDraw.setPenColor(Color.WHITE);
                            font2 = new Font("Arial", Font.BOLD, 40);
                            StdDraw.setFont(font2);


                            StdDraw.text(0.5, .60, "(1) Game 1");

                            StdDraw.setPenColor(Color.GREEN);
                            StdDraw.text(0.5, .50, "(2) Game 2");
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(0.5, 0.40, "(3) Game 3");
                            StdDraw.text(0.5, .30, "(E) Go Back");
                            StdDraw.show();


                        }

                        if (key == '3') {

                            saveSlot = 3;

                            StdDraw.clear(Color.BLACK);

                            StdDraw.setPenRadius(0.07);
                            StdDraw.setPenColor(Color.WHITE);
                            font2 = new Font("Arial", Font.BOLD, 40);
                            StdDraw.setFont(font2);


                            StdDraw.text(0.5, .60, "(1) Game 1");


                            StdDraw.text(0.5, .50, "(2) Game 2");

                            StdDraw.setPenColor(Color.GREEN);
                            StdDraw.text(0.5, 0.40, "(3) Game 3");
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(0.5, .30, "(E) Go Back");
                            StdDraw.show();


                        }

                    }


                }


            } else if (StdDraw.isKeyPressed(81)) {
                StdDraw.close();
                System.exit(0);
            }
        }

    }


}
