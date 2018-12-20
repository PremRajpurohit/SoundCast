package com.example.kimsarang.soundcast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MusicLibraryAdapter.OnItemClickListener {

    RecyclerView musicLibrary;
    LinearLayoutManager layoutManager;
    MusicLibraryAdapter musicLibraryAdapter;
    View bottomSheetLayout;
    Toolbar toolBar;
    BottomSheetBehavior bottomSheetBehavior;

    static ArrayList<Music> musicList;
    static ArrayList<ParseObject> parseObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        musicLibrary = findViewById(R.id.musicLibrary);

        bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        setupRecyclerView();
        populateData();
        setupBottomSheet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addMusic:
                startActivity(new Intent(this,UploadMusic.class));
                Toast.makeText(this,"Add Music Pressed",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        musicList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(HomeActivity.this);
        musicLibrary.setLayoutManager(layoutManager);

        musicLibraryAdapter= new MusicLibraryAdapter(this,musicList);
        musicLibrary.setAdapter(musicLibraryAdapter);
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            float o = 1f;
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i)
                {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetLayout.setBackgroundColor(Color.WHITE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetLayout.setBackgroundColor(getColor(R.color.colorPrimary));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void populateData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("songs_library");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                parseObjects = (ArrayList<ParseObject>) objects;
                for(ParseObject object : objects) {
                    if(object.getString("title") != null && object.getString("thumbnail") != null && object.getString("link") != null)
                    {
                        Music music = new Music();
                        music.setTitle(object.getString("title"));
                        music.setImage(object.getString("thumbnail"));
                        music.setMusic(object.getString("link"));
                        musicList.add(music);
                    }
                }

                musicLibraryAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClicked(View view) {
       Music music = musicList.get(Integer.valueOf(view.getTag().toString()));
       Intent playIntent = new Intent(this,PlayerActivity.class);
       Bundle bundle = new Bundle();
       bundle.putSerializable("music",music);
       bundle.putInt("index",musicList.indexOf(music));
       playIntent.putExtras(bundle);
       startActivity(playIntent);
    }
}
