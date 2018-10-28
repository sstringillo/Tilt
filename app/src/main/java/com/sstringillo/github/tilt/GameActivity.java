package com.sstringillo.github.tilt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private ImageView redArrow;
    private ImageView yellowArrow;
    private ImageView greenArrow;
    private ImageView blueArrow;
    //Rotation matrices to check if user tilted phone enough
    float[] rotationMatrix = new float[9];
    float[] prevRotationMatrix = new float[9];
    float[] AngleValues = new float[3];
    //Array to store values from accelerometer
    float[] AccelData = new float[3];
    //Array to store values from magnetic field
    float[] MagneticData = new float[3];
    int count = 0;
    long delayMilli = 3000;
    Handler timeHandler = new Handler();
    Runnable timer = new Runnable() {
        @Override
        public void run() {
         gameBreak();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Sets up game screen by registering sensors, hiding arrows and retrieving user high score if they have one
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        redArrow = (ImageView)findViewById(R.id.arrow_red_game);
        yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_game);
        greenArrow = (ImageView)findViewById(R.id.arrow_green_game);
        blueArrow = (ImageView)findViewById(R.id.arrow_blue_game);
        blueArrow.setVisibility(View.INVISIBLE);
        redArrow.setVisibility(View.INVISIBLE);
        yellowArrow.setVisibility(View.INVISIBLE);
        greenArrow.setVisibility(View.INVISIBLE);
        SharedPreferences pref = this.getSharedPreferences("High_score_pref",Context.MODE_PRIVATE);
        int storedHighScore = pref.getInt("Updated_high_score",0);
        TextView HighScoreCount = (TextView)findViewById(R.id.high_score_count_num);
        HighScoreCount.setText(String.valueOf(storedHighScore));
        gameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        start();
    }

    @Override
    public void onBackPressed() {
        //Do nothing in this activity since we do not want user exiting prematurely
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(sensorEvent.values, 0, AccelData, 0, 3);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(sensorEvent.values, 0, MagneticData, 0, 3);
                break;

            default:
                break;
        }
        //Checks if any arrow is visible, if not keep updating current rotation to be used as the control matrix (prevRotationMatrix)
        //Once an arrow is visible sensor checks for rotation and uses that for current position (rotationMatrix) to get angle difference
        if(blueArrow.getVisibility() == View.VISIBLE||redArrow.getVisibility() == View.VISIBLE||yellowArrow.getVisibility() == View.VISIBLE||greenArrow.getVisibility() == View.VISIBLE) {
            SensorManager.getRotationMatrix(rotationMatrix, null, AccelData, MagneticData);
            SensorManager.getAngleChange(AngleValues, rotationMatrix, prevRotationMatrix);
            arrowCheck(AngleValues[1], AngleValues[2]);
        }
        else{
            SensorManager.getRotationMatrix(prevRotationMatrix, null, AccelData, MagneticData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not used in this application
    }

    private void start() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 10000);
        timeHandler.postDelayed(timer,delayMilli);
    }

    private void stop() {
        mSensorManager.unregisterListener(this);
        timeHandler.removeCallbacks(timer);
    }

    //Checks if user correctly tilted the phone in the direction of the visible arrow
    //If they did, add 1 to the score and reset the countdown timer
    //Also decrease countdown timer to increase difficulty every point
    //If either user tilted the wrong way or time ran out, ends the game
    private void arrowCheck(float xAxis, float yAxis){
        if(delayMilli==500){
            delayMilli=1000;
            //When time to tilt reaches half a second reset time to 1 second
            //so when score reaches 100 game has easier and harder sections
        }
        TextView ScoreCount = (TextView) findViewById(R.id.score_count_num);
            if (greenArrow.getVisibility() == View.VISIBLE) {
                if(xAxis > 0.15) {
                    timeHandler.removeCallbacks(timer);
                    greenArrow.setVisibility(View.INVISIBLE);
                    count++;
                    delayMilli = delayMilli - 25;
                    ScoreCount.setText(String.valueOf(count));
                    gameLoop();
                }
                else if(xAxis<-0.25||yAxis>0.35||yAxis<-0.35){
                    gameBreak();
                }
            }
            if (yellowArrow.getVisibility() == View.VISIBLE) {
                if(xAxis < -0.15) {
                    timeHandler.removeCallbacks(timer);
                    yellowArrow.setVisibility(View.INVISIBLE);
                    count++;
                    delayMilli = delayMilli - 25;
                    ScoreCount.setText(String.valueOf(count));
                    gameLoop();
                }
                else if(xAxis>0.25||yAxis>0.35||yAxis<-0.35){
                    gameBreak();
                }
            }
            if (blueArrow.getVisibility() == View.VISIBLE) {
                if(yAxis > 0.25) {
                    timeHandler.removeCallbacks(timer);
                    blueArrow.setVisibility(View.INVISIBLE);
                    count++;
                    delayMilli = delayMilli - 25;
                    ScoreCount.setText(String.valueOf(count));
                    gameLoop();
                }
                else if(xAxis>0.35||xAxis<-0.35||yAxis<-0.25){
                    gameBreak();
                }
            }
            if (redArrow.getVisibility() == View.VISIBLE) {
                if(yAxis < -0.20) {
                    timeHandler.removeCallbacks(timer);
                    redArrow.setVisibility(View.INVISIBLE);
                    count++;
                    delayMilli = delayMilli - 25;
                    ScoreCount.setText(String.valueOf(count));
                    gameLoop();
                }
                else if(xAxis>0.35||xAxis<-0.35||yAxis>0.25){
                    gameBreak();
                }
            }

    }

    //Sets a random arrow to be visible on the screen and starts a countdown timer
    //to be used by arrowCheck
    private void gameLoop() {
        final int rand = (int) Math.floor(Math.random() * 4) + 1;
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (rand) {
                    case 1:
                        blueArrow.setVisibility(View.VISIBLE);
                        timeHandler.postDelayed(timer,delayMilli);
                        break;
                    case 2:
                        redArrow.setVisibility(View.VISIBLE);
                        timeHandler.postDelayed(timer,delayMilli);
                        break;
                    case 3:
                        yellowArrow.setVisibility(View.VISIBLE);
                        timeHandler.postDelayed(timer,delayMilli);
                        break;
                    case 4:
                        greenArrow.setVisibility(View.VISIBLE);
                        timeHandler.postDelayed(timer,delayMilli);
                        break;
                    default:
                        blueArrow.setVisibility(View.VISIBLE);
                        timeHandler.postDelayed(timer,delayMilli);
                        break;
                }
            }
        },500);
    }

    //Stops the sensor from retrieving data and ends the game by sending user to the end screen
    private void gameBreak() {
        stop();
        Intent endIntent = new Intent(GameActivity.this, EndActivity.class);
        endIntent.putExtra("Score",count);
        startActivity(endIntent);
        }
    }