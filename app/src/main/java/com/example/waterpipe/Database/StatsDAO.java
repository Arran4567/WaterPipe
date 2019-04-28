package com.example.waterpipe.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StatsDAO {

    @Insert
    void insert(Statistics stats);

    @Query("DELETE FROM stats_table WHERE difficulty = :difficulty")
    void deleteStat(String difficulty);

    @Query("SELECT * from stats_table ORDER BY difficulty ASC")
    LiveData<List<Statistics>> getAllStats();
}
