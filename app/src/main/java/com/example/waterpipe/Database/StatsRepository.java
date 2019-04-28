package com.example.waterpipe.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class StatsRepository {
    private StatsDAO mStatsDao;
    private LiveData<List<Statistics>> mAllStats;

    StatsRepository(Application application) {
        StatsRoomDatabase db = StatsRoomDatabase.getDatabase(application);
        mStatsDao = db.statsDAO();
        mAllStats = mStatsDao.getAllStats();
    }

    LiveData<List<Statistics>> getAllStats() {
        return mAllStats;
    }

    public void insert (Statistics stats) {
        new insertAsyncTask(mStatsDao).execute(stats);
    }


    public void delete (String difficulty){
        new deleteAsyncTask(mStatsDao).execute(difficulty);
    }

    private static class insertAsyncTask extends AsyncTask<Statistics, Void, Void> {

        private StatsDAO mAsyncTaskDao;

        insertAsyncTask(StatsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Statistics... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private StatsDAO mAsyncTaskDao;

        deleteAsyncTask(StatsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteStat(params[0]);
            return null;
        }
    }
}
