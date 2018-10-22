package com.sstringillo.github.tilt;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {
    //Used to make sure the same arrow is not repeated when flashing animation is played
    int randCheck = (int) Math.floor(Math.random()*4)+1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        disappearAnimation(flashingArrows());
        final Button StartButton = (Button)findViewById(R.id.start_button);
        final Button OptionButton = (Button)findViewById(R.id.options_button);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(HomeActivity.this,GameActivity.class);
                startActivity(gameIntent);
            }
        });
        OptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(HomeActivity.this,MenuActivity.class);
                startActivity(menuIntent);
            }
        });
    }

    //Animation to make the arrows fade away on the home screen
    private void disappearAnimation(final ImageView img){
        Animation disappear = new AlphaAnimation(1,0);
        disappear.setInterpolator(new AccelerateInterpolator());
        disappear.setDuration(300);

        disappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    img.setVisibility(View.INVISIBLE);
                    appearAnimation(img);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        img.startAnimation(disappear);
    }

    //Animation to make the arrows fade back in on the home screen
    private void appearAnimation(final ImageView img){
        Animation appear = new AlphaAnimation(0,1);
        appear.setInterpolator(new AccelerateInterpolator());
        appear.setDuration(200);

        appear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);
                disappearAnimation(flashingArrows());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        img.startAnimation(appear);
    }

    //Helper function to randomly choose an arrow to fade in and fade out
    private ImageView flashingArrows(){
        ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue);
        ImageView redArrow = (ImageView)findViewById(R.id.arrow_red);
        ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow);
        ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green);
        int rand = (int) Math.floor(Math.random()*4)+1;
        while(randCheck==rand){
            rand = (int) Math.floor(Math.random()*4)+1;
        }
        randCheck=rand;
        switch (rand){
            case 1: return blueArrow;
            case 2: return redArrow;
            case 3: return yellowArrow;
            case 4: return greenArrow;
            default: return blueArrow;
        }
    }

    @Override
    public void onBackPressed() {
        //Do nothing in this activity since we do not want user exiting back
        //to EndActivity if they already played. User can use home button to
        //exit app completely
    }
}
