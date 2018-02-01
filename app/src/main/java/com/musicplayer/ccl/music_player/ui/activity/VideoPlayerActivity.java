package com.musicplayer.ccl.music_player.ui.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.VideoItem;

import utils.LogUtils;

/**
 * Created by ccl on 18-2-1.
 */

public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    protected int layouId() {
        return R.layout.video_player;
    }

    @Override
    protected void initView() {
        videoView = (VideoView) findViewById(R.id.video_playerview);
    }

    @Override
    protected void initListener() {

        videoView.setOnPreparedListener(new OnVideoPreparedListener());


    }

    @Override
    protected void initData() {
        VideoItem videoItem = (VideoItem) getIntent().getSerializableExtra("videoItem");
       // LogUtils.e(getClass(),""+videoItem);
        videoView.setVideoPath(videoItem.getPath());
        //系统自带进度条
        //videoView.setMediaController(new MediaController(this));


    }

    @Override
    protected void processClick(View view) {

    }

    private class OnVideoPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
        }
    }
}
