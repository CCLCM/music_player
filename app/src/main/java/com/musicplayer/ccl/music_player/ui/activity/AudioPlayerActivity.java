package com.musicplayer.ccl.music_player.ui.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.AudioItem;
import com.musicplayer.ccl.music_player.service.AudioPlayerService;
import com.musicplayer.ccl.music_player.ui.activity.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class AudioPlayerActivity extends BaseActivity {

    private ArrayList<AudioItem> audioItems;
    private int mPostion;
    private ServiceAudioConnection mServerConnection;
    private static AudioPlayerService.AudioServiceBinder mAudioServerBinder;
    private ImageView iv_pause;

    @Override
    protected int layouId() {
        return R.layout.audio_player;
    }

    @Override
    protected void initView() {
        iv_pause = findViewById(R.id.audio_player_iv_pause);


    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = new Intent(getIntent());
        intent.setClass(this,AudioPlayerService.class);
        mServerConnection = new ServiceAudioConnection();
        bindService(intent, mServerConnection, Service.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.audio_player_iv_pause:
                wwitchPauseStatus();
                break;
        }

    }
    /**切换播放状态并更新暂停按钮的图片*/
    private void wwitchPauseStatus() {
        if (mAudioServerBinder.isPlaying()) {
            mAudioServerBinder.pause();
        } else {
            mAudioServerBinder.start();
        }
    }

    /**提供播放控制开端的功能*/
    private static class ServiceAudioConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mAudioServerBinder = (AudioPlayerService.AudioServiceBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    public void onDetachedFromWindow() {
        unbindService(mServerConnection);
    }
}
