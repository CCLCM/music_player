package com.musicplayer.ccl.music_player.ui.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.VideoItem;

import utils.LogUtils;

/**
 * Created by ccl on 18-2-1.
 */

public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;
    private ImageView iv_pause;
    private TextView tv_title;

    @Override
    protected int layouId() {
        return R.layout.video_player;
    }

    @Override
    protected void initView() {
        videoView = (VideoView) findViewById(R.id.video_playerview);
        iv_pause = findViewById(R.id.video_playerview_vi_pause);
        tv_title = findViewById(R.id.video_player_tv_tittle);
    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);
        videoView.setOnPreparedListener(new OnVideoPreparedListener());


    }

    @Override
    protected void initData() {
        VideoItem videoItem = (VideoItem) getIntent().getSerializableExtra("videoItem");
       // LogUtils.e(getClass(),""+videoItem);
        videoView.setVideoPath(videoItem.getPath());
        tv_title.setText(videoItem.getTitle());


        //系统自带进度条
        //videoView.setMediaController(new MediaController(this));


    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.video_playerview_vi_pause:
                switchPauseStatus();
                break;
        }

    }
    /**切换视频的播放状态*/
    private void switchPauseStatus() {
        if (videoView.isPlaying()) {
            videoView.pause();
        } else {
            videoView.start();
        }

        updatePauseBtn();

    }

   /**更新按钮暂停的图片*/
    private void updatePauseBtn() {

        if (videoView.isPlaying()) {
            iv_pause.setImageResource(R.drawable.video_pause_selector);
        } else {
            iv_pause.setImageResource(R.drawable.video_play_selector);
        }

    }

    private class OnVideoPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
            updatePauseBtn();
        }
    }
}
