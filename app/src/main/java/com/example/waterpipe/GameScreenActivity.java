package com.example.waterpipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;

public class GameScreenActivity extends AppCompatActivity {

    private SystemClock systemClock;
    private TextView tvTime;
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        int difficulty = getIntent().getIntExtra("difficulty", -1);
        TextView tv = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        switch (difficulty){
            case 0:
                tv.setText("Beginner");
                break;
            case 1:
                tv.setText("Intermediate");
                break;
            case 2:
                tv.setText("Expert");
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        generatePipes();
        populateGrid(pipes);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
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

    private void populateGrid(ArrayList<Pipe> pipes){
        for(int i = 0; i < 49; i++){
            String tile = "tile" + i;
            Pipe p = pipes.get(i);
            int id = getResources().getIdentifier(tile, "id", getPackageName());
            if(id != 0) {
                ImageView iv = findViewById(id);
                iv.setRotation((p.getRotation()*90));
                if(p.isBend()){
                    iv.setImageResource(R.drawable.bent);
                }else{
                    iv.setImageResource(R.drawable.straight);
                }
            }
        }
    }

    public void rotateTile(View view){
        int viewID = view.getId();
        ImageView iv = findViewById(viewID);
        String tileName = view.getResources().getResourceName(viewID);
        int pipeID = 0;
        try {
            pipeID = Integer.parseInt (tileName.replaceFirst("^.*\\D",""));
        } catch (Exception e) {}
        Pipe p = pipes.get(pipeID);
        if(p.getRotation()<3) {
            p.setRotation(p.getRotation() + 1);
        }else{
            p.setRotation(0);
        }
        iv.setRotation(p.getRotation()*90);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            tvTime.setText("Time: " + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    public void stopTimer(View view){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you wish to go back? Your game data will be lost.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do nothing
                            return;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
