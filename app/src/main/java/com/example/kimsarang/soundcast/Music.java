package com.example.kimsarang.soundcast;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.parse.ParseFile;

import java.io.Serializable;

public class Music implements Serializable {

    private String title;
    private String image;
    private String music;

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
