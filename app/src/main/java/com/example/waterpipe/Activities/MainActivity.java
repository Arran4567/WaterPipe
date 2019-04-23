package com.example.waterpipe.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.waterpipe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openDifficultyScreen(View view){
        Intent intent = new Intent(this, DifficultySelectionActivity.class);
        startActivity(intent);
    }

    public void openStatisticsScreen(View view){
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
}
