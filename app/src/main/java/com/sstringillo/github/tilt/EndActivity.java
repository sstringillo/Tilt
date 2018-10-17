package com.sstringillo.github.tilt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int count = getIntent().getIntExtra("Score",0);
        TextView ScoreCount = (TextView) findViewById(R.id.score_count_num);
        ScoreCount.setText(String.valueOf(count));
        SharedPreferences pref = this.getSharedPreferences("High_score_pref",Context.MODE_PRIVATE);
        int storedHighScore = pref.getInt("Updated_high_score",0);
        if(count>storedHighScore) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("Updated_high_score", count);
            editor.apply();
        }
        else {
            TextView scoreView = (TextView)findViewById(R.id.high_score_display);
            scoreView.setVisibility(View.INVISIBLE);
        }
        final Button PlayButton = (Button)findViewById(R.id.play_again_button);
        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(EndActivity.this,GameActivity.class);
                startActivity(gameIntent);
            }
        });
        final Button MenuButton = (Button)findViewById(R.id.menu_button);
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(EndActivity.this,HomeActivity.class);
                startActivity(gameIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*
        *Do nothing in this activity since we do not want unexpected actions from user
        *Want user to only be able to play again or go to menu
        */
    }
}
