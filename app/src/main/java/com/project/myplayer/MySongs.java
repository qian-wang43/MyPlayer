package com.project.myplayer;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MySongs {
    public static ArrayList<Song> getAllSongs(Context context) {
        ArrayList<Song> songs = null;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA },
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);
        songs = new ArrayList<Song>();
        if (cursor.moveToFirst()) {
            do {
                String fileName=cursor.getString(1);
                String name=cursor.getString(2);
                int duration=cursor.getInt(3);
                String artist=cursor.getString(4);
                String album=cursor.getString(5);
                String year=cursor.getString(6);
                if (year== null)
                   year="unknow";
                String type=cursor.getString(7).trim();
                if ("audio/mpeg".equals(type)) {

                    type="mp3";

                } else if ("audio/x-ms-wma".equals(type)){

                    type="wma";

                }

                String fileUrl=cursor.getString(9);
                Song song=new Song(name, artist, duration, fileUrl);

                songs.add(song);

            } while (cursor.moveToNext());

            cursor.close();

        }

        return songs;

    }


}
