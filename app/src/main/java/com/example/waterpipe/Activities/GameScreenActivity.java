package com.example.waterpipe.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waterpipe.Database.Statistics;
import com.example.waterpipe.Database.StatsViewModel;
import com.example.waterpipe.Objects.Grid;
import com.example.waterpipe.Objects.Pipe;
import com.example.waterpipe.R;

import java.util.List;

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

    private static StatsViewModel mViewModel;
    Statistics dbStats = new Statistics("","","","","","");
    int difficulty;
    String txtDifficulty;
    private String numComp;
    private String avgTime;
    private String bestTime;
    private String avgRots;
    private String leastRots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        difficulty = getIntent().getIntExtra("difficulty", -1);
        txtDifficulty = determineDifficulty(difficulty);
        TextView tv = findViewById(R.id.tvTitle);
        tv.setText(txtDifficulty);
        determineDifficulty(difficulty);
        tvTime = findViewById(R.id.tvTime);

        createGrid(difficulty);
        TextView tvNumSol = findViewById(R.id.tvNumSol);
        tvNumSol.setText("Number of solutions: " + numSolutions);

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        mViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        getStats(txtDifficulty);
    }

    private void getStats(final String difficulty){
        final LiveData<List<Statistics>> statsList = mViewModel.getAllStats();
        statsList.observe(this, new Observer<List<Statistics>>() {
            @Override
            public void onChanged(@Nullable List<Statistics> statistics) {
                for(Statistics s : statistics){
                    if (s.getDifficulty().equals(difficulty)){
                        dbStats = s;
                    }
                }
            }
        });
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

    private String determineDifficulty(int difficulty) {
        switch (difficulty) {
            case 0:
                return "Beginner";
            case 1:
                return "Intermediate";
            case 2:
                return "Expert";
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
                    saveStats();
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

    private void saveStats() {
        Statistics stats;
        if(dbStats.getDifficulty().equals("")){
            stats = new Statistics(txtDifficulty, timeTaken, timeTaken, "" + numRotations, "" + numRotations, "1");
            mViewModel.insert(stats);
        }else{
            String avgTime = calcAvgTime(dbStats.getAvgTime(), Integer.parseInt(dbStats.getNumComp()));
            String bestTime = checkBestTime(dbStats.getBestTime());
            String avgRots = calcAvgRots(dbStats.getAvgRots(), Integer.parseInt(dbStats.getNumComp()));
            String leastRots = checkLeastRots(dbStats.getLeastRots());
            String numComp = "" + (Integer.parseInt(dbStats.getNumComp()) + 1);
            stats = new Statistics(txtDifficulty, avgTime, bestTime, "" + avgRots, "" + leastRots, numComp);
            mViewModel.delete(txtDifficulty);
            mViewModel.insert(stats);
        }
    }

    private String checkLeastRots(String leastRots) {
        if(numRotations > Integer.parseInt(leastRots)){
            return leastRots;
        }else{
            return "" + numRotations;
        }
    }

    private String calcAvgRots(String avgRots, int numComp) {
        float ftAvgRots = Float.parseFloat(avgRots);
        float totalRots = ftAvgRots*numComp + (float) numRotations;
        String newAvgRots = "" + (totalRots/(numComp+1));
        return newAvgRots;
    }

    private String checkBestTime(String bestTime) {
        String splitTime[] = bestTime.split(":");
        long totalMilliseconds = Long.parseLong(splitTime[0])*60000 + Long.parseLong(splitTime[1])*1000 + Long.parseLong(splitTime[2]);
        long newBestTime = totalMilliseconds;
        if(totalMilliseconds > updatedTime){
            newBestTime = updatedTime;
        }
        int secs = (int) (newBestTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (newBestTime % 1000);
        return mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
    }

    private String calcAvgTime(String avgTime, int numComp) {
        String splitTime[] = avgTime.split(":");
        long totalTime = (Long.parseLong(splitTime[0])*60000 + Long.parseLong(splitTime[1])*1000 + Long.parseLong(splitTime[2]))*numComp + updatedTime;
        long newAvgTime = totalTime/(numComp+1);
        int secs = (int) (newAvgTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (newAvgTime % 1000);
        return mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
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
