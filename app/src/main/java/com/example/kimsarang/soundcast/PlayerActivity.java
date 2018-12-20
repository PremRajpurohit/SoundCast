package com.example.kimsarang.soundcast;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Time;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    CheckBox playPause;
    ImageButton nextPlay, previousPlay;
    ImageView trackThumbnail;
    TextView trackTitle,trackDuration;
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    Music music;
    SeekBar seekBar;
    int curr_track;
    int total_tracks;
    long total_time;
    long duration[];
    int d = 0;

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

        duration = new long[2];

        setContentView(R.layout.activity_player);
        seekBar = findViewById(R.id.seekBar);

        total_tracks = HomeActivity.musicList.size();
        trackTitle = findViewById(R.id.musicTitle);
        trackThumbnail = findViewById(R.id.musicThumbnail);
        trackDuration = findViewById(R.id.musicDuration);
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    long mProgress = TimeUnit.MILLISECONDS.toSeconds(getCurrentDuration());
                    long d = (progress > mProgress ? progress - mProgress : mProgress - progress);
                    Toast.makeText(PlayerActivity.this,String.valueOf(d),Toast.LENGTH_SHORT).show();
                    mediaPlayer.seekTo((int)TimeUnit.SECONDS.toMillis(d));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        playPause.setChecked(true);
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
            playPause.setChecked(false);
        }
    }

    public void getMetaData(Object...params) {
        total_time = (int)params[0];
        duration [0] = TimeUnit.MILLISECONDS.toMinutes(total_time);
        duration [1] = TimeUnit.MILLISECONDS.toSeconds(total_time);
        trackDuration.setText(MessageFormat.format("{0} : {1}",duration[0],duration[1]));
    }

    public long getCurrentDuration() {
        long mCurrent = mediaPlayer.getCurrentPosition();
        return mCurrent / total_time * 100;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextPlay:
                resumeMusic();
                playMusic(curr_track < total_tracks - 1 ? ++curr_track : 0);
                Toast.makeText(PlayerActivity.this,"Next",Toast.LENGTH_SHORT).show();
                break;
            case R.id.previousPlay:
                resumeMusic();
                playMusic(curr_track < 0 ? total_tracks - 1 : --curr_track);
                //TODO: When Previous button pressed
                Toast.makeText(PlayerActivity.this,"Previous",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        getMetaData(mp.getDuration());
        mp.start();
        seekBar.setMax((int) duration[1]);
        progressBar.setVisibility(View.GONE);
        playPause.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
