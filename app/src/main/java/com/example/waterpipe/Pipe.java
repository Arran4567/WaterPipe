package com.example.waterpipe;

public class Pipe {
    private boolean bend;
    private int direction;

    public Pipe(){
        bend = getRandomBoolean();
        direction = (int)(Math.random()*4);
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    public boolean getBend(){return bend;}

    public int getDirection(){
        return direction;
    }

    public void setBend(boolean newBend){
        this.bend = newBend;
    }

    public void setDirection(int newDirection){
        this.direction = newDirection;
    }
}
