package com.example.waterpipe.Database;

import android.arch.persistence.room.*;
import android.content.Context;

@Database(entities = {Statistics.class}, version = 1, exportSchema = false)
public abstract class StatsRoomDatabase extends RoomDatabase {

    public abstract StatsDAO statsDAO();
    private static StatsRoomDatabase INSTANCE;

    static StatsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StatsRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StatsRoomDatabase.class, "stats_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
