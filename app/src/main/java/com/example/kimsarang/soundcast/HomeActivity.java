package com.example.kimsarang.soundcast;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button showPlayer;
    RecyclerView musicLibrary;
    LinearLayoutManager layoutManager;
    MusicLibraryAdapter musicLibraryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showPlayer = findViewById(R.id.showPlayer);
        musicLibrary = findViewById(R.id.musicLibrary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showPlayerIntent = new Intent(HomeActivity.this,PlayerActivity.class);
                startActivity(showPlayerIntent);
            }
        });
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        musicLibraryAdapter = new MusicLibraryAdapter(HomeActivity.this);//TODO: What Recycler view hold.
    }
}
