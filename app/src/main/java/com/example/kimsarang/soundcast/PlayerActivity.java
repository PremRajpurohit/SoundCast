package com.example.kimsarang.soundcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    CheckBox playPause;
    ImageButton nextPlay, previousPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playPause = findViewById(R.id.playPause);
        nextPlay = findViewById(R.id.nextPlay);
        previousPlay = findViewById(R.id.previousPlay);
        nextPlay.setOnClickListener(this);
        previousPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPause.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(PlayerActivity.this,"Playing",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PlayerActivity.this,"Stopped",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextPlay:
                //TODO: When Next button pressed
                Toast.makeText(PlayerActivity.this,"Next ->",Toast.LENGTH_SHORT).show();
                break;
            case R.id.previousPlay:
                //TODO: When Previous button pressed
                Toast.makeText(PlayerActivity.this,"Previous <-",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
