package org.sxchinacourt.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.sxchinacourt.R;


public class VideoPlayActivity extends AppCompatActivity {
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        videoView = (VideoView) findViewById(R.id.videoView);
        String path = getIntent().getStringExtra("path");
        if("".equals(path)){
            Toast.makeText(this,"未拿到路径",Toast.LENGTH_SHORT).show();
        }else {
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoPath(path);
        }
    }
}
