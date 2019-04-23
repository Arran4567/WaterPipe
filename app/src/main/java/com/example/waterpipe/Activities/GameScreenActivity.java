package com.example.waterpipe.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waterpipe.Objects.Grid;
import com.example.waterpipe.Objects.Pipe;
import com.example.waterpipe.R;

public class GameScreenActivity extends AppCompatActivity {

    private TextView tvTime;
    private long startTime = 0L;
    private Grid grid = new Grid();

    int numSolutions = 0;
    int numRotations = 0;
    String timeTaken;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        int difficulty = getIntent().getIntExtra("difficulty", -1);
        determineDifficulty(difficulty);
        tvTime = findViewById(R.id.tvTime);

        createGrid(difficulty);

        TextView tvNumSol = findViewById(R.id.tvNumSol);
        tvNumSol.setText("Number of solutions: " + numSolutions);

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void createGrid(int difficulty) {
        numSolutions = 0;
        Grid searchGrid = new Grid();
        searchGrid.setPipes(grid.getPipes());
        DFSUtil(searchGrid);
        switch (difficulty) {
            case 0:
                while (!(numSolutions < 7 && numSolutions >= 5)) {
                    numSolutions = 0;
                    grid = new Grid();
                    searchGrid.setPipes(grid.getPipes());
                    DFSUtil(searchGrid);
                }
                break;
            case 1:
                while (!(numSolutions < 5 && numSolutions >= 3)) {
                    numSolutions = 0;
                    grid = new Grid();
                    searchGrid.setPipes(grid.getPipes());
                    DFSUtil(searchGrid);
                }
                break;
            case 2:
                while (!(numSolutions < 3 && numSolutions > 0)) {
                    numSolutions = 0;
                    grid = new Grid();
                    searchGrid.setPipes(grid.getPipes());
                    DFSUtil(searchGrid);
                }
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        populateGridView();
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
        checkCompletion(grid.getPipe(0));
        numRotations++;
    }

    private void checkCompletion(Pipe src) {
        src.setVisited(true);
        for (Pipe p : grid.findSurroundTiles(src)) {
            if (grid.checkTileConnectivity(src, p) && p.getId() == 48) {
                if (p.getLinks().get(0).equals("down") || p.getLinks().get(1).equals("down")) {
                    stopTimer();
                    Dialog alert = completedPuzzle();
                    alert.show();
                    return;
                }
            } else if (!p.isVisited() && grid.checkTileConnectivity(src, p)) {
                checkCompletion(p);
            }
        }
        src.setVisited(false);
    }

    private void DFSUtil(Grid searchGrid) {
        while (searchGrid.getPipe(0).getRotation() != 0) {
            searchGrid.rotatePipe(searchGrid.getPipe(0));
        }
        DFS(searchGrid, searchGrid.getPipe(0));
    }

    private void DFS(Grid searchGrid, Pipe src) {
        src.setVisited(true);
        for (Pipe p : searchGrid.findSurroundTiles(src)) {
            if (p.isBend()) {
                for (int i = 0; i < 4; i++) {
                    if (!p.isVisited() && !searchGrid.checkTileConnectivity(src, p)) {
                        searchGrid.rotatePipe(p);
                    } else if (searchGrid.checkTileConnectivity(src, p) && p.getId() == 48) {
                        if (p.getLinks().get(0).equals("down") || p.getLinks().get(1).equals("down")) {
                            numSolutions++;
                        }
                        searchGrid.rotatePipe(p);
                    } else if (!p.isVisited() && searchGrid.checkTileConnectivity(src, p)) {
                        DFS(searchGrid, p);
                        searchGrid.rotatePipe(p);
                    }
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    if (!p.isVisited() && !searchGrid.checkTileConnectivity(src, p)) {
                        searchGrid.rotatePipe(p);
                    } else if (searchGrid.checkTileConnectivity(src, p) && p.getId() == 48) {
                        if (p.getLinks().get(0).equals("down") || p.getLinks().get(1).equals("down")) {
                            numSolutions++;
                        }
                        searchGrid.rotatePipe(p);
                    } else if (!p.isVisited() && searchGrid.checkTileConnectivity(src, p)) {
                        DFS(searchGrid, p);
                        searchGrid.rotatePipe(p);
                    }
                }
            }
        }
        src.setVisited(false);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            timeTaken = mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
            tvTime.setText(timeTaken);
            customHandler.postDelayed(this, 0);
        }
    };

    public void stopTimer() {
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
                            Intent intent = new Intent(getBaseContext(), DifficultySelectionActivity.class);
                            startActivity(intent);
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

    public Dialog completedPuzzle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Congratulations you completed the puzzle in " + numRotations + " rotations, with a time of: " + timeTaken + ".\nDo you wish to return to the main screen or see your statistics?")
                .setPositiveButton("Main Screen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Statistics", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(), StatisticsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        return builder.create();
    }
}
