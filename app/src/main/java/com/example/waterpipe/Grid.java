package com.example.waterpipe;

import java.util.ArrayList;
import java.util.Stack;

public class Grid {
    private ArrayList<Pipe> pipes = new ArrayList<>();

    public Grid() {
        generatePipes();
    }

    private void generatePipes() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                int[] pos = {x, y};
                Pipe p = new Pipe(pos);
                pipes.add(p);
            }
        }
    }

    public void rotatePipe(Pipe p) {
        if (p.getRotation() < 3) {
            p.setRotation(p.getRotation() + 1);
        } else {
            p.setRotation(0);
        }
        p.updateLinks();
    }

    public boolean checkTileConnectivity(Pipe pipe1, Pipe pipe2) {
        if (pipe1.getLinks().get(0).equals("up") && pipe1.getLinks().get(0).equals("down")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("down") && pipe1.getLinks().get(0).equals("up")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("left") && pipe1.getLinks().get(0).equals("right")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("right") && pipe1.getLinks().get(0).equals("left")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("up") && pipe1.getLinks().get(0).equals("down")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("down") && pipe1.getLinks().get(0).equals("up")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("left") && pipe1.getLinks().get(0).equals("right")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("right") && pipe1.getLinks().get(0).equals("left")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("up") && pipe1.getLinks().get(1).equals("down")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("down") && pipe1.getLinks().get(1).equals("up")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("left") && pipe1.getLinks().get(1).equals("right")) {
            return true;
        } else if (pipe1.getLinks().get(0).equals("right") && pipe1.getLinks().get(1).equals("left")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("up") && pipe1.getLinks().get(1).equals("down")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("down") && pipe1.getLinks().get(1).equals("up")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("left") && pipe1.getLinks().get(1).equals("right")) {
            return true;
        } else if (pipe1.getLinks().get(1).equals("right") && pipe1.getLinks().get(1).equals("left")) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Pipe> findSurroundTiles(Pipe p) {
        ArrayList<Pipe> surroundingPipes = new ArrayList<>();
        int x = p.getPosition()[0];
        int y = p.getPosition()[1];
        if (x - 1 != -1) {
            surroundingPipes.add(findByPos(x - 1, y));
        }
        if (x + 1 != 7) {
            surroundingPipes.add(findByPos(x + 1, y));
        }
        if (y - 1 != -1) {
            surroundingPipes.add(findByPos(x, y - 1));
        }
        if (y + 1 != 7) {
            surroundingPipes.add(findByPos(x, y + 1));
        }
        return surroundingPipes;
    }

    private Pipe findByPos(int x, int y) {
        Pipe pipe = new Pipe();
        for (Pipe p : pipes) {
            if (p.getPosition()[0] == x && p.getPosition()[1] == y) {
                pipe = p;
            }
        }
        return pipe;
    }

    public Pipe getPipe(int id) {
        return pipes.get(id);
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public void setPipes(ArrayList<Pipe> pipes) {
        this.pipes = pipes;
    }
}
