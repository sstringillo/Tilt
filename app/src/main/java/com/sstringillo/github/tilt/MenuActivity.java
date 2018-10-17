package com.sstringillo.github.tilt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Button HowToPlay = (Button)findViewById(R.id.how_to_play);
        final Button DeleteHighScore = (Button)findViewById(R.id.delete_high_score);
        final Button VersionImp = (Button)findViewById(R.id.version_improvements);

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

        HowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),"TODO Still",Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void deleteHighScore(){
        SharedPreferences pref = getSharedPreferences("High_score_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("Updated_high_score");
        editor.apply();
    }
}
