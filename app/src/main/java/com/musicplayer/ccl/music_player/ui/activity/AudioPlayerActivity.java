package com.musicplayer.ccl.music_player.ui.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;

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

    @Override
    protected int layouId() {
        return R.layout.audio_player;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

//        audioItems = (ArrayList<AudioItem>) getIntent().getSerializableExtra("audioItems");
//        mPostion = getIntent().getIntExtra("postion",-1);
//        AudioItem audioItem = audioItems.get(mPostion);
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(audioItem.getPath());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(getIntent());
        intent.setClass(this,AudioPlayerService.class);
        mServerConnection = new ServiceAudioConnection();
        bindService(intent, mServerConnection, Service.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void processClick(View view) {

    }

    private static class ServiceAudioConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

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
