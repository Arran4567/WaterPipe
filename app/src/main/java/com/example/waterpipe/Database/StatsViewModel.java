package com.example.waterpipe.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class StatsViewModel extends AndroidViewModel {

    private StatsRepository mRepository;
    private LiveData<List<Statistics>> mAllStats;

    public StatsViewModel (Application application) {
        super(application);
        mRepository = new StatsRepository(application);
        mAllStats = mRepository.getAllStats();
    }

    public LiveData<List<Statistics>> getAllStats(){ return mAllStats; }

    public void insert(Statistics stats){ mRepository.insert(stats);}

    public void delete(String difficulty){ mRepository.delete(difficulty);}

}
