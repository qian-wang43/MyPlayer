package com.project.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private SongDBHelper songDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private List<Song>songs;
    private FloatingActionButton btnDelete;
    private SongAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        songDBHelper=new SongDBHelper(FavoriteActivity.this);
        sqLiteDatabase=songDBHelper.getWritableDatabase();
        songs=new ArrayList<Song>();
        query();
        mAdapter = new SongAdapter(FavoriteActivity.this, songs);
        mListView=(ListView)findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox=findViewById(position);
                checkBox.toggle();
            }
        });
        initBtnDelete();
    }

    private void query(){
        Cursor cs= sqLiteDatabase.query( "Songs", null, null, null, null, null, null);
        if(cs.moveToNext()){
            for(int i = 0; i<cs.getCount(); i++){
                cs.move(i);
                String name=cs.getString(cs.getColumnIndex("Name" ));
                int duration=cs.getInt(cs.getColumnIndex( "Duration"));
                String fileUrl=cs.getString(cs.getColumnIndex( "FileUrl"));
                String artist=cs.getString(cs.getColumnIndex( "Artist"));
                Song song=new Song(name, artist,duration, fileUrl);
                songs.add(song);
            }
        }

    }


    private void initBtnDelete(){
        btnDelete=findViewById(R.id.del_fab);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongDBHelper songDBHelper=new SongDBHelper(FavoriteActivity.this);
                SQLiteDatabase sqLiteDatabase=songDBHelper.getWritableDatabase();
                for(int i=0;i<songs.size();i++){
                    CheckBox checkBox=findViewById(i);

                    if(checkBox.isChecked()){
                        Song song=songs.get(i);
                        sqLiteDatabase.delete("Songs", "FileUrl=?", new String[]{song.getFileUrl()});

                    }
                }
                songs.clear();
                query();
                mListView.setAdapter(mAdapter);
            }
        });

    }
}
