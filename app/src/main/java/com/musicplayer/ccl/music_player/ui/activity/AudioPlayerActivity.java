package com.musicplayer.ccl.music_player.ui.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.AudioItem;
import com.musicplayer.ccl.music_player.service.AudioPlayerService;

import java.util.ArrayList;

import utils.LogUtils;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class AudioPlayerActivity extends BaseActivity {

    private ArrayList<AudioItem> audioItems;
    private int mPostion;
    private ServiceAudioConnection mServerConnection;
    private static AudioPlayerService.AudioServiceBinder mAudioServerBinder;
    private ImageView iv_pause;
    private OnAudioEventReceiver onAudioEventReceiver;
    private TextView tv_tittle;
    private TextView tv_arties;

    @Override
    protected int layouId() {
        return R.layout.audio_player;
    }

    @Override
    protected void initView() {
        tv_tittle = findViewById(R.id.main_video_tv_title);
        tv_arties = findViewById(R.id.audio_player_iv_arties);

        iv_pause = findViewById(R.id.audio_player_iv_pause);



    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);

        //注册广播
        IntentFilter filter = new IntentFilter("com.chencl.mobileplayer.audio_player");
        onAudioEventReceiver = new OnAudioEventReceiver();
        registerReceiver(onAudioEventReceiver, filter);

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
                switchPauseStatus();
                break;
        }

    }
    /**切换播放状态并更新暂停按钮的图片*/
    private void switchPauseStatus() {
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
            mAudioServerBinder.play();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private class OnAudioEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AudioItem audioItem = (AudioItem) intent.getSerializableExtra("audioitem");
            tv_tittle.setText(audioItem.getTitle());
            tv_arties.setText(audioItem.getArties());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServerConnection);
        unregisterReceiver(onAudioEventReceiver);
    }
}
