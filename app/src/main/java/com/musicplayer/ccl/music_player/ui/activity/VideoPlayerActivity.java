package com.musicplayer.ccl.music_player.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.VideoItem;


import utils.LogUtils;
import utils.StringUtils;

/**
 * Created by ccl on 18-2-1.
 */

public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;
    private ImageView iv_pause;
    private TextView tv_title;
    private OnVideoReceiver onVideoReceiver;
    private ImageView iv_battery;
    private TextView tv_system_time;
    private static final int MSG_UPDATE_SYSTEM_TIME =0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_SYSTEM_TIME:
                    startUpdateSystemTime();
                    break;
            }
        }
    };
    @Override
    protected int layouId() {
        return R.layout.video_player;
    }

    @Override
    protected void initView() {
        videoView = (VideoView) findViewById(R.id.video_playerview);
        iv_pause = findViewById(R.id.video_playerview_vi_pause);
        tv_title = findViewById(R.id.video_player_tv_tittle);
        iv_battery = findViewById(R.id.video_player_iv_battery);
        tv_system_time = findViewById(R.id.video_player_iv_time);
    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);
        videoView.setOnPreparedListener(new OnVideoPreparedListener());

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        onVideoReceiver = new OnVideoReceiver();
        registerReceiver(onVideoReceiver,filter);


    }

    private class OnVideoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取系统变量
            int level = intent.getIntExtra("level",0);
            updateBatteryBtn(level);
        }


    }

    /**根据当前系统使用的图片*/
    private void updateBatteryBtn(int level) {
        if (level < 10) {
            iv_battery.setImageResource(R.drawable.ic_battery_0);
        } else if (level < 40) {
            iv_battery.setImageResource(R.drawable.ic_battery_20);
        }  else if (level < 60) {
            iv_battery.setImageResource(R.drawable.ic_battery_40);
        }  else if (level < 80) {
            iv_battery.setImageResource(R.drawable.ic_battery_60);
        }  else if (level < 100) {
            iv_battery.setImageResource(R.drawable.ic_battery_80);
        }  else if (level == 100) {
            iv_battery.setImageResource(R.drawable.ic_battery_100);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onVideoReceiver);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void initData() {
        VideoItem videoItem = (VideoItem) getIntent().getSerializableExtra("videoItem");
       // LogUtils.e(getClass(),""+videoItem);
        videoView.setVideoPath(videoItem.getPath());
        tv_title.setText(videoItem.getTitle());


        //系统自带进度条
        //videoView.setMediaController(new MediaController(this));

        startUpdateSystemTime();


    }
    /**更新系统时间,并延迟一段时间之后再次更新*/
    private void startUpdateSystemTime() {
        tv_system_time.setText(StringUtils.formatSystemTime());
        handler.sendEmptyMessageDelayed(MSG_UPDATE_SYSTEM_TIME,500);
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
