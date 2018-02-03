package com.musicplayer.ccl.music_player.ui.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.AudioItem;
import com.musicplayer.ccl.music_player.service.AudioPlayerService;

import java.util.ArrayList;

import utils.LogUtils;
import utils.StringUtils;

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
    private ImageView iv_wave;
    private TextView tv_position;
    private static  final  int MSG_UPDATE_POSITION =0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_POSITION:
                    startUpdatePosition();
                    break;
            }

        }
    };
    private SeekBar sk_position;
    private ImageView iv_pre;
    private ImageView iv_next;

    @Override
    protected int layouId() {
        return R.layout.audio_player;
    }

    @Override
    protected void initView() {
        tv_tittle = findViewById(R.id.main_video_tv_title);
        tv_arties = findViewById(R.id.audio_player_iv_arties);
        iv_wave = findViewById(R.id.audio_player_iv_wave);
        tv_position = findViewById(R.id.audio_player_tv_position);
        sk_position = findViewById(R.id.audio_player_sk_posion);
        iv_pre = findViewById(R.id.audio_player_iv_pre);
        iv_next = findViewById(R.id.audio_player_iv_next);


        iv_pause = findViewById(R.id.audio_player_iv_pause);



    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);

        //注册广播
        IntentFilter filter = new IntentFilter("com.chencl.mobileplayer.audio_player");
        onAudioEventReceiver = new OnAudioEventReceiver();
        registerReceiver(onAudioEventReceiver, filter);

        sk_position.setOnSeekBarChangeListener(new OnAudioSeekBarChangeListener());

        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = new Intent(getIntent());
        intent.setClass(this,AudioPlayerService.class);
        mServerConnection = new ServiceAudioConnection();
        bindService(intent, mServerConnection, Service.BIND_AUTO_CREATE);
        startService(intent);

        //开启示波器
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_wave.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.audio_player_iv_pause:
                switchPauseStatus();
                break;
            case R.id.audio_player_iv_pre:
                playPre();
                break;
            case R.id.audio_player_iv_next:
                playNext();
                break;
        }

    }
    /**播放下一首歌*/
    private void playNext() {
        mAudioServerBinder.playNext();
    }
    /**播放上一首歌*/
    private void playPre() {
        mAudioServerBinder.playPre();

    }

    /**切换播放状态并更新暂停按钮的图片*/
    private void switchPauseStatus() {
        if (mAudioServerBinder.isPlaying()) {
            mAudioServerBinder.pause();
            mHandler.removeMessages(MSG_UPDATE_POSITION);
        } else {
            mAudioServerBinder.start();
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION,500);
        }

        updatePauseBtn();
    }


    /**根据播放状态转换播放图标的状态*/
    private void updatePauseBtn() {
        if (mAudioServerBinder.isPlaying()) {
            //正在播放
            iv_pause.setImageResource(R.drawable.audio_pause_selector);
        } else {
            //暂停状态
            iv_pause.setImageResource(R.drawable.audio_play_selector);
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

    /**进度条进行变更*/
    private class OnAudioSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (!b){
                return;
            }
            //跳转到指定的进度
            mAudioServerBinder.seekTo(i);
            updatePosition(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class OnAudioEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AudioItem audioItem = (AudioItem) intent.getSerializableExtra("audioitem");
            //初始化歌曲名称
            tv_tittle.setText(audioItem.getTitle());
            //初始化歌手名字
            tv_arties.setText(audioItem.getArties());

            //更新暂停播放按钮的实现
            updatePauseBtn();
            
            //开启进度更新
            sk_position.setMax(mAudioServerBinder.getDuration());
            startUpdatePosition();
        }
    }

    /**更新播放进度 ,并延迟一段时间之后再更新*/
    private void startUpdatePosition() {
        int position = mAudioServerBinder.getCurrentPosition();
        updatePosition(position);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION,500);
    }

    private void updatePosition(int position) {
        int duration = mAudioServerBinder.getDuration();
        String positionStr = StringUtils.formatDuration(position);
        String durationStr = StringUtils.formatDuration(duration);
        tv_position.setText(positionStr+"/"+durationStr);
        sk_position.setProgress(position);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServerConnection);
        unregisterReceiver(onAudioEventReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }
}
