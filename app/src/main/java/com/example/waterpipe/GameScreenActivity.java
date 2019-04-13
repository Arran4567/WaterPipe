package com.example.waterpipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class GameScreenActivity extends AppCompatActivity {

    private TextView tvTime;
    private long startTime = 0L;
    private Grid grid = new Grid();

    Stack<Pipe> searchStack = new Stack<>();
    int numSolutions = 0;

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
        boolean possibleSolution = DFSUtil(searchGrid);
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

    private boolean DFSUtil(Grid searchGrid) {
        while (searchGrid.getPipe(0).getRotation() != 0) {
            searchGrid.rotatePipe(searchGrid.getPipe(0));
        }
        boolean possibleSolution = DFS(searchGrid, searchGrid.getPipe(0));
        return possibleSolution;
    }


    private boolean DFS(Grid searchGrid, Pipe src) {
        // Create a stack for DFS
        Stack<Pipe> stack = new Stack<>();
        // Push the current source node
        stack.push(src);

        while (!stack.empty()) {
            // Pop a vertex from stack and print it
            Pipe s = stack.pop();

            // Stack may contain same vertex twice. So
            // we need to print the popped item only
            // if it is not visited.
            if (!src.isVisited()) {
                Log.d("id", "" + s.getId());
                s.setVisited(true);
            }

            // Get all adjacent vertices of the popped vertex s
            // If a adjacent has not been visited, then push it
            // to the stack.
            for (Pipe p : searchGrid.findSurroundTiles(s)) {
                for (int i = 0; i < 3; i++) {
                    if (!searchGrid.checkTileConnectivity(s, p)) {
                        searchGrid.rotatePipe(p);
                    } else if (p.getId() == 48){
                        if(p.getLinks().get(0).equals("down") || p.getLinks().get(1).equals("down")) {
                            numSolutions++;
                        }
                    } else {
                        if (!p.isVisited()) {
                            stack.push(p);
                            searchGrid.rotatePipe(p);
                        }
                    }
                }
            }
        }
        if(numSolutions > 0){
            return true;
        }else {
            return false;
        }
    }
    /*private boolean DLS(Grid searchGrid, Pipe src) {
        searchStack.add(src);
        src.setVisited(true);
        if (src.getId() == 48) {
            if (searchGrid.getPipe(48).getLinks().get(0).equals("down") || searchGrid.getPipe(48).getLinks().get(1).equals("down")) {
                return true;
            } else {
                return false;
            }
        }

        while (!searchStack.empty()) {
            Pipe currentNode = searchStack.peek();
            ArrayList<Pipe> surroundingPipes;
            surroundingPipes = searchGrid.findSurroundTiles(currentNode);
            for (Pipe p : surroundingPipes) {
                if (!p.isVisited()) {
                    for (int i = 0; i < 3; i++) {
                        if (searchGrid.checkTileConnectivity(src, p)) {
                            if (DLS(searchGrid, p)) {
                                numSolutions++;
                            }
                        }
                        searchGrid.rotatePipe(p);
                    }
                }
            }
        }
        src.setVisited(false);
        searchStack.pop();
        if(numSolutions > 0){
            return true;
        }else{
            return false;
        }
    }*/

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

        public void stopTimer (View view){
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }

        @Override
        public boolean onKeyDown ( int keyCode, KeyEvent event){
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
