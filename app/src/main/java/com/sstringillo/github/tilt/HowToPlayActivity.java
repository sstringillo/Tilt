package com.sstringillo.github.tilt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class HowToPlayActivity extends AppCompatActivity implements SensorEventListener {
    //Values same as GameActivity
    private SensorManager mSensorManager;
    float[] rotationMatrix = new float[9];
    float[] prevRotationMatrix = new float[9];
    float[] AngleValues = new float[3];
    float[] AccelData = new float[3];
    float[] MagneticData = new float[3];
    Handler instructionHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplay);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue_howtoplay);
        final ImageView redArrow = (ImageView)findViewById(R.id.arrow_red_howtoplay);
        final ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_howtoplay);
        final ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green_howtoplay);
        final TextView HowToPlayInstruction = (TextView)findViewById(R.id.How_to_play_instruction);
        final TextView HowToPlayInstructionCont = (TextView)findViewById(R.id.How_to_play_instruction_cont);
        final TextView HowToPlayInstructionCorrect = (TextView)findViewById(R.id.How_to_play_instruction_correct);
        final TextView HowToPlayInstructionsComplete = (TextView)findViewById(R.id.How_to_play_instruction_complete);
        blueArrow.setVisibility(View.INVISIBLE);
        redArrow.setVisibility(View.INVISIBLE);
        yellowArrow.setVisibility(View.INVISIBLE);
        greenArrow.setVisibility(View.INVISIBLE);
        HowToPlayInstructionCont.setVisibility(View.INVISIBLE);
        HowToPlayInstructionCorrect.setVisibility(View.INVISIBLE);
        HowToPlayInstructionsComplete.setVisibility(View.INVISIBLE);
        //Starts correct sequence of Views for tutorial
        instructionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blueArrow.setVisibility(View.VISIBLE);
                HowToPlayInstruction.setVisibility(View.INVISIBLE);
                HowToPlayInstructionCont.setVisibility(View.VISIBLE);
            }
        },3000);
    }

    @Override
    //Same as GameActivity
    public void onSensorChanged(SensorEvent sensorEvent) {
        final ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue_howtoplay);
        final ImageView redArrow = (ImageView)findViewById(R.id.arrow_red_howtoplay);
        final ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_howtoplay);
        final ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green_howtoplay);
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
        if(blueArrow.getVisibility() == View.VISIBLE||redArrow.getVisibility() == View.VISIBLE||yellowArrow.getVisibility() == View.VISIBLE||greenArrow.getVisibility() == View.VISIBLE) {
            SensorManager.getRotationMatrix(rotationMatrix, null, AccelData, MagneticData);
            SensorManager.getAngleChange(AngleValues, rotationMatrix, prevRotationMatrix);
            instructionLoop(AngleValues[1], AngleValues[2]);
        }
        else{
            SensorManager.getRotationMatrix(prevRotationMatrix, null, AccelData, MagneticData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not used in this application
    }

    //Teachers user how to play game by showing arrows and TextViews with instructions on how to play
    private void instructionLoop(float xAxis, float yAxis){
        final ImageView blueArrow = (ImageView)findViewById(R.id.arrow_blue_howtoplay);
        final ImageView redArrow = (ImageView)findViewById(R.id.arrow_red_howtoplay);
        final ImageView yellowArrow = (ImageView)findViewById(R.id.arrow_yellow_howtoplay);
        final ImageView greenArrow = (ImageView)findViewById(R.id.arrow_green_howtoplay);
        final TextView HowToPlayInstructionCont = (TextView)findViewById(R.id.How_to_play_instruction_cont);
        final TextView HowToPlayInstructionCorrect = (TextView)findViewById(R.id.How_to_play_instruction_correct);
        if(yAxis>0.25&&blueArrow.getVisibility()==View.VISIBLE){
            blueArrow.setVisibility(View.INVISIBLE);
            HowToPlayInstructionCont.setVisibility(View.INVISIBLE);
            HowToPlayInstructionCorrect.setVisibility(View.VISIBLE);
            redArrow.setVisibility(View.VISIBLE);
            yellowArrow.setVisibility(View.VISIBLE);
            greenArrow.setVisibility(View.VISIBLE);
        }
        if(yAxis<-0.25&&redArrow.getVisibility()==View.VISIBLE){
            redArrow.setVisibility(View.INVISIBLE);
        }
        if(xAxis>0.20&&greenArrow.getVisibility()==View.VISIBLE){
            greenArrow.setVisibility(View.INVISIBLE);
        }
        if(xAxis<-0.20&&yellowArrow.getVisibility()==View.VISIBLE){
            yellowArrow.setVisibility(View.INVISIBLE);
        }
        if(blueArrow.getVisibility()==View.INVISIBLE&&redArrow.getVisibility()==View.INVISIBLE&&yellowArrow.getVisibility()==View.INVISIBLE&&greenArrow.getVisibility()==View.INVISIBLE){
            TextView HowToPlayInstructionsComplete = (TextView)findViewById(R.id.How_to_play_instruction_complete);
            HowToPlayInstructionCorrect.setVisibility(View.INVISIBLE);
            HowToPlayInstructionsComplete.setVisibility(View.VISIBLE);
            instructionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  HowToPlayBreak();
                }
            },2000);
        }
    }

    //Sends user back to HomeActivity when they finish the tutorial
    private void HowToPlayBreak(){
        stop();
        Intent homeIntent = new Intent(HowToPlayActivity.this, HomeActivity.class);
        startActivity(homeIntent);
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

    private void start() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 10000);
    }

    private void stop() {
        mSensorManager.unregisterListener(this);
    }
}