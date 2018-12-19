package com.example.kimsarang.soundcast;

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("VSPdpDKRMND382hqIRFIaiVLgbkhM0E1rL32l1SQ")
                .clientKey("NnOwo2ejzrpQJD98uF9weupAo2AFH305DCOLVaBQ")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
