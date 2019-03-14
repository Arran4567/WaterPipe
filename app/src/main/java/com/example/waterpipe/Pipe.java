package com.example.waterpipe;

public class Pipe {
    private int rotation;
    private boolean bend;
    private int[] position;

    public Pipe(int[] pos){
        position = pos;
        if(Math.random() < 0.75){
            bend = true;
        }else{
            bend = false;
        }
        rotation = (int)(Math.random()*4);
    }

    public boolean isBend() {
        return bend;
    }

    public void setBend(boolean bend) {
        this.bend = bend;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
