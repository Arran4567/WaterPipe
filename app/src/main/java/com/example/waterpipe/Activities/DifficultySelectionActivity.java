package com.example.waterpipe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.waterpipe.R;

public class DifficultySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        switch (view.getId()) {
            case R.id.button2:
                intent.putExtra("difficulty", 0);
                break;
            case R.id.button3:
                intent.putExtra("difficulty", 1);
                break;
            case R.id.button4:
                intent.putExtra("difficulty", 2);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        startActivity(intent);
        finish();
    }
}