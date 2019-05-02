package com.example.waterpipe.Objects;

import java.util.ArrayList;

public class Pipe {
    private int pipeID;
    private int rotation;
    private ArrayList<String> links = new ArrayList<>();
    private boolean bend;
    private int[] position;
    private boolean visited;

    public Pipe() {
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getId() {
        return pipeID;
    }

    public Pipe(int[] pos, int id) {
        pipeID = id;
        position = pos;
        visited = false;
        if (Math.random() < 0.7) {
            bend = true;
        } else {
            bend = false;
        }
        rotation = (int) (Math.random() * 4);
        this.updateLinks();
        //A rotation of 0 will be "L" shaped
        //Incrementing rotation by 1 rotates pipe 90 degrees clockwise
    }

    public boolean isBend() {
        return bend;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void updateLinks() {
        this.links.clear();
        if (bend) {
            switch (rotation) {
                case 0:
                    this.links.add("up");
                    this.links.add("right");
                    break;
                case 1:
                    this.links.add("right");
                    this.links.add("down");
                    break;
                case 2:
                    this.links.add("down");
                    this.links.add("left");
                    break;
                case 3:
                    this.links.add("left");
                    this.links.add("up");
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        } else {
            switch (rotation) {
                case 0:
                    this.links.add("up");
                    this.links.add("down");
                    break;
                case 1:
                    this.links.add("left");
                    this.links.add("right");
                    break;
                case 2:
                    this.links.add("up");
                    this.links.add("down");
                    break;
                case 3:
                    this.links.add("left");
                    this.links.add("right");
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        }
    }

    public int[] getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    //The following methods are only used for testing.
    public void setPipeID(int pipeID) {
        this.pipeID = pipeID;
    }

    public void setBend(boolean bend) {
        this.bend = bend;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
