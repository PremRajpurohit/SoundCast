package com.example.kimsarang.soundcast;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    CheckBox playPause;
    ImageButton nextPlay, previousPlay;
    ImageView trackThumbnail;
    TextView trackTitle;
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    Music music;
    int curr_track,total_tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                music = (Music) bundle.getSerializable("music");
                curr_track = bundle.getInt("index");
            }
        }

        setContentView(R.layout.activity_player);


        total_tracks = HomeActivity.musicList.size();

        trackTitle = findViewById(R.id.musicTitle);
        trackThumbnail = findViewById(R.id.musicThumbnail);

        playPause = findViewById(R.id.playPause);
        nextPlay = findViewById(R.id.nextPlay);
        previousPlay = findViewById(R.id.previousPlay);
        progressBar = findViewById(R.id.progressBar);


        nextPlay.setOnClickListener(this);
        previousPlay.setOnClickListener(this);


        trackTitle.setText(music.getTitle());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playMusic(-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPause.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    resumeMusic();
                    Toast.makeText(PlayerActivity.this,"Playing",Toast.LENGTH_SHORT).show();
                }
                else {
                    pauseMusic();
                    Toast.makeText(PlayerActivity.this,"Paused",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer = null;
    }

    private void playMusic(int position) {
        if(position != -1) {
            music = HomeActivity.musicList.get(position);
            curr_track = position;
        }

        stopMusic();

        Picasso.with(this).load(music.getImage()).into(trackThumbnail);
        trackTitle.setText(music.getTitle());

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(music.getMusic());
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resumeMusic() {
        mediaPlayer.start();
    }

    private void stopMusic() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void pauseMusic() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextPlay:
                playMusic(curr_track < total_tracks - 1 ? ++curr_track : 0);
                Toast.makeText(PlayerActivity.this,"Next",Toast.LENGTH_SHORT).show();
                break;
            case R.id.previousPlay:
                playMusic(curr_track < 0 ? total_tracks - 1 : --curr_track);
                //TODO: When Previous button pressed
                Toast.makeText(PlayerActivity.this,"Previous",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        progressBar.setVisibility(View.GONE);
        playPause.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what)
        {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this,"Unknown",Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}
