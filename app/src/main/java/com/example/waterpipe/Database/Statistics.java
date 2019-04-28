package com.example.waterpipe.Database;

import android.arch.persistence.room.*;
import android.support.annotation.*;

@Entity(tableName = "stats_table")
public class Statistics {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "numComp")
    private String numComp;
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    @ColumnInfo(name = "avgTime")
    private String avgTime;
    @ColumnInfo(name = "bestTime")
    private String bestTime;
    @ColumnInfo(name = "avgRots")
    private String avgRots;
    @ColumnInfo(name = "leastRots")
    private String leastRots;

    public Statistics(String difficulty, String avgTime, String bestTime, String avgRots, String leastRots, String numComp) {
        this.difficulty = difficulty;
        this.avgTime = avgTime;
        this.bestTime = bestTime;
        this.avgRots = avgRots;
        this.leastRots = leastRots;
        this.numComp = numComp;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getAvgTime() {
        return this.avgTime;
    }

    public String getBestTime() {
        return this.bestTime;
    }

    public String getAvgRots() {
        return this.avgRots;
    }

    public String getLeastRots() {
        return this.leastRots;
    }

    public String getNumComp() {
        return this.numComp;
    }
}
