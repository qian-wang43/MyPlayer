package com.project.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class  PlayActivity extends AppCompatActivity {
    Button playBtn, nextBtn, previousBtn, pauseBtn;
    TextView songName;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    String sName;
    ArrayList<File> mySongs;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        previousBtn = (Button) findViewById(R.id.previousBtn);
        songName = (TextView) findViewById(R.id.songName);

        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);


//        Intent i = getIntent();
        Bundle incomingData = getIntent().getExtras();
        if(incomingData != null) {
            Song s = (Song) getIntent().getSerializableExtra("selectedObject");
            songName.setText(s.getName());
            songName.setSelected(true);
            position=incomingData.getInt("pos",0);
            //Uri u = Uri.parse(s.getArtist().toString());
            Uri u = Uri.parse(mySongs.get(position).toString());
            mp = MediaPlayer.create(getApplicationContext(),u);
            //mp=new MediaPlayer();
            //mp.setDataSource(song.getFileUrl())
        }

        // Media Player
//        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        // Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {

        if (!mp.isPlaying()) {
            // Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause);

        } else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }

    }

    public void nextBtnClick(View view){
        mp.stop();
        mp.release();
        position = ((position+1)%mySongs.size());
        Uri u = Uri.parse(mySongs.get(position).toString());
        mp=mp.create(getApplicationContext(),u);
        sName=mySongs.get(position).getName().toString();
        songName.setText(sName);
        mp.start();
    }

    public void previousBtnClick(View view){
        mp.stop();
        mp.release();
        position = ((position-1)<0) ? (mySongs.size()-1):(position-1);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mp=mp.create(getApplicationContext(),u);
        sName=mySongs.get(position).getName().toString();
        songName.setText(sName);
        mp.start();
    }

}

