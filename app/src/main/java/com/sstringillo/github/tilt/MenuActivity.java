package com.sstringillo.github.tilt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Button HowToPlay = (Button)findViewById(R.id.how_to_play);
        final Button DeleteHighScore = (Button)findViewById(R.id.delete_high_score);
        final Button VersionImp = (Button)findViewById(R.id.version_improvements);

        //Creates AlertDialog to appear to make sure user wants to delete high score
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.simple_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteHighScore();
            }
        });
        builder.setNegativeButton(R.string.simple_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setMessage(R.string.Delete_check).setTitle(R.string.Deletion_ask);
        DeleteHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Creates AlertDialog to inform user of improvements in that latest version of the application
        final AlertDialog.Builder builderImp = new AlertDialog.Builder(this);
        builderImp.setPositiveButton(R.string.Got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builderImp.setMessage(R.string.Improvements1_01).setTitle(R.string.Version_improvement_nodash);
        VersionImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builderImp.create();
                dialog.show();
            }
        });
        //Starts HowToPlayActivity when pressed
        HowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HowToPlayIntent = new Intent(MenuActivity.this,HowToPlayActivity.class);
                startActivity(HowToPlayIntent);
            }
        });
    }

    //Helper function to delete the saved high score when the user chooses the Delete High Score option
    private void deleteHighScore(){
        SharedPreferences pref = getSharedPreferences("High_score_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("Updated_high_score");
        editor.apply();
    }
}
