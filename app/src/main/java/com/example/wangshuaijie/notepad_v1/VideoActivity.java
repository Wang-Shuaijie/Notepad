package com.example.wangshuaijie.notepad_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Administrator on 2018/6/5/005.
 */

public class VideoActivity extends AppCompatActivity {

    private String videoPath;
    private VideoView videoView;
    private MediaController mediaController;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent=getIntent();
        videoPath=intent.getStringExtra("videoPath");
        videoView=(VideoView)findViewById(R.id.videoView);
        mediaController = new MediaController(this);
       videoView.setVideoPath(videoPath);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.start();

    }
}
