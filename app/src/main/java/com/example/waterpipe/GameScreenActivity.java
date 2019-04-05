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

import java.util.Stack;

public class GameScreenActivity extends AppCompatActivity {

    private TextView tvTime;
    private long startTime = 0L;
    private Grid grid = new Grid();

    Stack<Pipe> searchStack = new Stack<>();

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        determineDifficulty(getIntent().getIntExtra("difficulty", -1));
        tvTime = findViewById(R.id.tvTime);
        populateGridView();
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        Grid searchGrid = new Grid();
        searchGrid.setPipes(grid.getPipes());
        int numSolutions = IDDFS(searchGrid, 49);
        TextView tvNumSol = findViewById(R.id.tvNumSol);
        tvNumSol.setText("" + numSolutions);
    }

    private void determineDifficulty(int difficulty) {
        TextView tv = findViewById(R.id.tvTitle);
        switch (difficulty) {
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
    }

    private void populateGridView() {
        for (int i = 0; i < 49; i++) {
            String tile = "tile" + i;
            Pipe p = grid.getPipe(i);
            int id = getResources().getIdentifier(tile, "id", getPackageName());

            if (id != 0) {
                ImageView iv = findViewById(id);
                iv.setRotation((p.getRotation() * 90));
                if (p.isBend()) {
                    iv.setImageResource(R.drawable.bent);
                } else {
                    iv.setImageResource(R.drawable.straight);
                }
            }
        }
    }

    public void rotateTile(View view) {
        int viewID = view.getId();
        ImageView iv = findViewById(viewID);
        String tileName = view.getResources().getResourceName(viewID);
        int pipeID = 0;
        try {
            pipeID = Integer.parseInt(tileName.replaceFirst("^.*\\D", ""));
        } catch (Exception e) {
        }
        Pipe p = grid.getPipe(pipeID);
        grid.rotatePipe(p);
        iv.setRotation(grid.getPipe(pipeID).getRotation() * 90);
    }

    private int IDDFS(Grid searchGrid, int max_depth) {
        int numSolutions = 0;
        while(searchGrid.getPipe(0).getRotation() != 0){
            searchGrid.rotatePipe(searchGrid.getPipe(0));
        }
        for (int limit = 0; limit < max_depth; limit++) {
            if (DLS(searchGrid, searchGrid.getPipe(0), limit)) {
                numSolutions++;
            }
        }
        return numSolutions;
    }


    private boolean DLS(Grid searchGrid, Pipe src, int limit) {
        searchStack.add(src);
        src.setVisited(true);
        if (src.equals(searchGrid.getPipe(48))) {
            if (searchGrid.getPipe(48).getLinks().get(0).equals("down")) {
                return true;
            } else if (searchGrid.getPipe(48).getLinks().get(1).equals("down")) {
                return true;
            }
        }

        if(limit<=0){
            return false;
        }

        while (!searchStack.empty()) {
            Pipe currentNode = searchStack.pop();
            for (Pipe p : searchGrid.findSurroundTiles(currentNode)) {
                if(!p.isVisited()){
                    limit--;
                    for (int i = 0; i < 3; i++) {
                        if(searchGrid.checkTileConnectivity(src, p)){
                            if (DLS(searchGrid, p, limit)) {
                                return true;
                            }
                        }
                        searchGrid.rotatePipe(p);
                    }
                }
            }
        }
        src.setVisited(false);
        return false;
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

    public void stopTimer(View view) {
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
