package com.project.myplayer;

import android.app.Application;

public class MyApplication extends Application {

    ////get the music list from the phone from the MySongs
    private MySongs mySongs = new MySongs();

    public MySongs getMySongs() {
        return mySongs;
    }

    public void setMySongs(MySongs mySongs) {
        this.mySongs = mySongs;
    }
}
