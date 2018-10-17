package com.sstringillo.github.tilt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    public float[] rotationMatrix = new float[9];
    public float[] OriValues = new float[3];
    public float[] AccelData = new float[3];
    public float[] MagneticData = new float[3];
    public int count = 0;
    public long delayMilli = 3000;
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
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ImageView redArrow = (ImageView)findViewById(R.id.arrow_red_game);
        ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_game);
        ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green_game);
        ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue_game);
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
        SensorManager.getRotationMatrix(rotationMatrix,null,AccelData,MagneticData);
        SensorManager.getOrientation(rotationMatrix,OriValues);
        arrowCheck(OriValues[1],OriValues[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Accuracy will remain constant so this function is unnecessary
    }

    public void start() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 10000);
        timeHandler.postDelayed(timer,delayMilli);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        timeHandler.removeCallbacks(timer);
    }


    public void arrowCheck(float xAxis, float yAxis){
        ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue_game);
        ImageView redArrow = (ImageView)findViewById(R.id.arrow_red_game);
        ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_game);
        ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green_game);
        TextView ScoreCount = (TextView) findViewById(R.id.score_count_num);
            if (xAxis > 0.35 && greenArrow.getVisibility() == View.VISIBLE) {
                timeHandler.removeCallbacks(timer);
                greenArrow.setVisibility(View.INVISIBLE);
                count++;
                delayMilli = delayMilli-25;
                ScoreCount.setText(String.valueOf(count));
                gameLoop();
            }
            if (xAxis < -0.35 && yellowArrow.getVisibility() == View.VISIBLE) {
                timeHandler.removeCallbacks(timer);
                yellowArrow.setVisibility(View.INVISIBLE);
                count++;
                delayMilli = delayMilli-25;
                ScoreCount.setText(String.valueOf(count));
                gameLoop();
            }
            if (yAxis > 0.27 && blueArrow.getVisibility() == View.VISIBLE) {
                timeHandler.removeCallbacks(timer);
                blueArrow.setVisibility(View.INVISIBLE);
                count++;
                delayMilli = delayMilli-25;
                ScoreCount.setText(String.valueOf(count));
                gameLoop();
            }
            if (yAxis < -0.27 && redArrow.getVisibility() == View.VISIBLE) {
                timeHandler.removeCallbacks(timer);
                redArrow.setVisibility(View.INVISIBLE);
                count++;
                delayMilli = delayMilli-25;
                ScoreCount.setText(String.valueOf(count));
                gameLoop();
            }

    }

    public void gameLoop() {
        final ImageView blueArrow = (ImageView) findViewById(R.id.arrow_blue_game);
        final ImageView redArrow = (ImageView) findViewById(R.id.arrow_red_game);
        final ImageView yellowArrow = (ImageView) findViewById(R.id.arrow_yellow_game);
        final ImageView greenArrow = (ImageView) findViewById(R.id.arrow_green_game);
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

    public void gameBreak() {
        Intent endIntent = new Intent(GameActivity.this, EndActivity.class);
        endIntent.putExtra("Score",count);
        startActivity(endIntent);
        }
    }