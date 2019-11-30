
package com.project.myplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends BaseAdapter {

    Activity mActivity;
    List<Song> mySongs;

    public SongAdapter(Activity mActivity,  List<Song> mySongs){
        this.mActivity = mActivity;
        this.mySongs = mySongs;
    }

    @Override
    public int getCount() {
        return mySongs.size();
    }

    @Override
    public Song getItem(int position) {
        if(mySongs==null)
            return null;
        return mySongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.song_oneline, viewGroup, false);
        TextView tv_name = view.findViewById(R.id.song_name);
        TextView tv_artist = view.findViewById(R.id.song_artist);
        ImageView iv_picture = view.findViewById(R.id.imageView);
        CheckBox checkBox=view.findViewById(R.id.checkBox);
        checkBox.setId(position);
        Song s = this.getItem(position);
        tv_name.setText(s.getName());
        tv_artist.setText(s.getArtist());
        iv_picture.setImageResource(R.drawable.ic_music_note);


        return view;
    }


}
