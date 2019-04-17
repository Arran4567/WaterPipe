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
                Pipe p = new Pipe(pos, ((7 * x) + y));
                pipes.add(p);
            }
        }
    }

    public void rotatePipe(Pipe p) {
        if(p.isBend()){
            if (p.getRotation() < 3) {
                p.setRotation(p.getRotation() + 1);
            } else {
                p.setRotation(0);
            }
        }else{
            if (p.getRotation() < 1) {
                p.setRotation(p.getRotation() + 1);
            } else {
                p.setRotation(0);
            }
        }
        p.updateLinks();
    }

    public boolean checkTileConnectivity(Pipe pipe1, Pipe pipe2) {
        for(Pipe i: getConnectedPipes(pipe1)){
            if(i.equals(pipe2)){
                for(Pipe j: getConnectedPipes(pipe2)){
                    if (j.equals(pipe1)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<Pipe> getConnectedPipes(Pipe pipe){
        ArrayList<Pipe> connectedPipes = new ArrayList<>();
        int[] posAbove = {pipe.getPosition()[0] - 1, pipe.getPosition()[1]};
        int[] posRight = {pipe.getPosition()[0], pipe.getPosition()[1] + 1};
        int[] posBelow = {pipe.getPosition()[0] + 1, pipe.getPosition()[1]};
        int[] posLeft = {pipe.getPosition()[0],pipe.getPosition()[1]-1};
        if(pipe.isBend()){
            switch (pipe.getRotation()){
                case 0:
                    connectedPipes.add(this.findByPos(posAbove[0], posAbove[1]));
                    connectedPipes.add(this.findByPos(posRight[0], posRight[1]));
                    break;
                case 1:
                    connectedPipes.add(this.findByPos(posRight[0], posRight[1]));
                    connectedPipes.add(this.findByPos(posBelow[0], posBelow[1]));
                    break;
                case 2:
                    connectedPipes.add(this.findByPos(posBelow[0], posBelow[1]));
                    connectedPipes.add(this.findByPos(posLeft[0], posLeft[1]));
                    break;
                case 3:
                    connectedPipes.add(this.findByPos(posLeft[0], posLeft[1]));
                    connectedPipes.add(this.findByPos(posAbove[0], posAbove[1]));
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        }else{
            switch (pipe.getRotation()){
                case 0:
                    connectedPipes.add(this.findByPos(posAbove[0], posAbove[1]));
                    connectedPipes.add(this.findByPos(posBelow[0], posBelow[1]));
                    break;
                case 1:
                    connectedPipes.add(this.findByPos(posLeft[0], posLeft[1]));
                    connectedPipes.add(this.findByPos(posRight[0], posRight[1]));
                    break;
                case 2:
                    connectedPipes.add(this.findByPos(posAbove[0], posAbove[1]));
                    connectedPipes.add(this.findByPos(posBelow[0], posBelow[1]));
                    break;
                case 3:
                    connectedPipes.add(this.findByPos(posLeft[0], posLeft[1]));
                    connectedPipes.add(this.findByPos(posRight[0], posRight[1]));
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        }
        return connectedPipes;
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
        this.pipes.clear();
        for (Pipe p: pipes) {
            this.pipes.add(p);
        }
    }
}
