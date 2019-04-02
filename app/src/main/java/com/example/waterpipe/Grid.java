package com.example.waterpipe;
import java.util.ArrayList;

public class Grid {
    private ArrayList<Pipe> pipes = new ArrayList<>();

    public Grid(){
        generatePipes();
    }

    private void generatePipes(){
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 7; y++){
                int[] pos = {x,y};
                Pipe p = new Pipe(pos);
                pipes.add(p);
            }
        }
    }

    public void rotatePipe(int pipeID){
        Pipe p = pipes.get(pipeID);
        if(p.getRotation()<3) {
            p.setRotation(p.getRotation() + 1);
        }else{
            p.setRotation(0);
        }
    }

    public Pipe[] findConnectedTiles(int pipeID){
        Pipe p = pipes.get(pipeID);
        int x = p.getPosition()[0];
        int y = p.getPosition()[1];
        Pipe pipe1;
        Pipe pipe2;
        if(p.isBend()){
            switch (p.getRotation()){
                case 0:
                    pipe1 = findByPos(x, y-1);
                    pipe2 = findByPos(x+1, y);
                    break;
                case 1:
                    pipe1 = findByPos(x+1, y);
                    pipe2 = findByPos(x, y+1);
                    break;
                case 2:
                    pipe1 = findByPos(x, y+1);
                    pipe2 = findByPos(x-1, y);
                    break;
                case 3:
                    pipe1 = findByPos(x-1, y);
                    pipe2 = findByPos(x, y-1);
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        }else{
            switch (p.getRotation()){
                case 0:
                    pipe1 = findByPos(x, y-1);
                    pipe2 = findByPos(x, y+1);
                    break;
                case 1:
                    pipe1 = findByPos(x-1, y);
                    pipe2 = findByPos(x+1, y);
                    break;
                case 2:
                    pipe1 = findByPos(x, y-1);
                    pipe2 = findByPos(x, y+1);
                    break;
                case 3:
                    pipe1 = findByPos(x-1, y);
                    pipe2 = findByPos(x+1, y);
                    break;
                default:
                    throw new RuntimeException("Unknown Rotation");
            }
        }
        Pipe[] pipes = {pipe1, pipe2};
        return pipes;
    }

    private Pipe findByPos(int x, int y){
        Pipe pipe = new Pipe();
        for (Pipe p : pipes) {
            if(p.getPosition()[0] == x && p.getPosition()[1] == y){
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
