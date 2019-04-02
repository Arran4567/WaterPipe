package com.example.waterpipe;

public class Pipe {
    private int rotation;
    private boolean bend;
    private int[] position;

    public Pipe(){}

    public Pipe(int[] pos){
        position = pos;
        if(Math.random() < 0.75){
            bend = true;
        }else{
            bend = false;
        }
        rotation = (int)(Math.random()*4);
        //A rotation of 0 will be "L" shaped
        //Incrementing rotation by 1 rotates pipe 90 degrees clockwise
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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
