package com.project.myplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addFab;
    ListView mListView;
    SongAdapter mAdapter;
    List<Song> allSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFab = findViewById(R.id.addfab);
        mListView = findViewById(R.id.list_view);
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            allSongs=MySongs.getAllSongs(this);
            if(allSongs.size()==0)
                Toast.makeText(this,"No song in this phone", Toast.LENGTH_SHORT).show();
            else{

                mAdapter = new SongAdapter(MainActivity.this, allSongs);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CheckBox checkBox=findViewById(position);
                        checkBox.toggle();
                    }
                });

                addFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SongDBHelper songDBHelper=new SongDBHelper(MainActivity.this);
                        SQLiteDatabase sqLiteDatabase=songDBHelper.getWritableDatabase();

                        for(int i=0;i<allSongs.size();i++){
                            CheckBox checkBox=findViewById(i);

                            if(checkBox.isChecked()){
                                Song song=allSongs.get(i);
                                ContentValues cv= new ContentValues();
                                cv.put("Name", song.getName());
                                cv.put("Duration", song.getDuration());
                                cv.put("Artist", song.getArtist());
                                cv.put("FileUrl", song.getFileUrl());
                                sqLiteDatabase.insert("Songs", null,cv);
                            }
                        }
                        Intent intent=new Intent(MainActivity.this,FavoriteActivity.class);
                        startActivity(intent);
                    }
                });

            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    allSongs=MySongs.getAllSongs(this);
                    if(allSongs.size()==0)
                        Toast.makeText(this,"No song in this phone", Toast.LENGTH_SHORT).show();
                    else{

                        mAdapter = new SongAdapter(MainActivity.this, allSongs);
                        mListView.setAdapter(mAdapter);
                    }
                } else {
                    Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();
                    boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(!b) {
                         Toast.makeText(this,"User checked the \"Not Remind Next Time\" option", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                default:
                    break;
        }
    }

}
