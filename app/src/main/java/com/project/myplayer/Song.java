package com.project.myplayer;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    private String name;
    private String artist;
    private int duration;
    private String fileUrl;
//    private int pictureId;


    public Song(String name, String artist, int duration, String fileUrl) {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.fileUrl = fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
