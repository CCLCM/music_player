package com.musicplayer.ccl.music_player.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.musicplayer.ccl.music_player.view.VideoView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.VideoItem;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


import java.util.ArrayList;

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
    private static final int MSG_UPDATE_POSION =1;
    private static final int MSG_HIDE_CONTROLOR =2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_SYSTEM_TIME:
                    LogUtils.e("","MSG_UPDATE_SYSTEM_TIME");
                    startUpdateSystemTime();
                    break;
                case MSG_UPDATE_POSION:
                    LogUtils.e("","MSG_UPDATE_POSION");
                    startUpdatePosion();
                    break;
                case MSG_HIDE_CONTROLOR:
                    LogUtils.e("","MSG_HIDE_CONTROLOR");
                    hideControl();
                    break;
            }
        }
    };
    private SeekBar video_volume;
    private AudioManager mAudioManager;
    private ImageView iv_mute;
    private int mCurrentVolume;
    private float mStartY;
    private int mStartVolume;
    private View alpha_cover;
    private float mStartAlpha;
    private TextView tv_posion;
    private TextView tv_duration;
    private SeekBar sk_posion;
    private OnVideoSeekBarChangeListener onSeekBarChangeListener;
    private ImageView iv_pre;
    private ImageView iv_next;
    private ArrayList<VideoItem> mVideoItems;
    private int mPosition;
    private LinearLayout ll_top;
    private LinearLayout ll_bottom;
    private GestureDetector gestureDetector;
    private boolean isShowControlor;
    private ImageView iv_fullscreen;

    @Override
    protected int layouId() {
        return R.layout.video_player;
    }

    @Override
    protected void initView() {
        videoView = (VideoView) findViewById(R.id.video_playerview);
        tv_title = findViewById(R.id.video_player_tv_tittle);
        iv_battery = findViewById(R.id.video_player_iv_battery);
        tv_system_time = findViewById(R.id.video_player_iv_time);
        video_volume = findViewById(R.id.video_player_sk_volume);
        iv_mute = findViewById(R.id.video_player_iv_mute);
        alpha_cover = findViewById(R.id.video_player_alpha_cover);
        tv_posion = findViewById(R.id.video_player_tv_posion);
        tv_duration = findViewById(R.id.video_player_tv_duration);
        sk_posion = findViewById(R.id.video_player_sk_posion);
        iv_pause = findViewById(R.id.video_playerview_vi_pause);
        iv_pre = findViewById(R.id.video_player_iv_pre);
        iv_next = findViewById(R.id.video_player_iv_next);
        ll_top = findViewById(R.id.video_player_ll_top);
        ll_bottom = findViewById(R.id.video_player_ll_bottom);
        iv_fullscreen = findViewById(R.id.video_player_iv_fullscreen);

    }

    @Override
    protected void initListener() {
        iv_pause.setOnClickListener(this);
        videoView.setOnPreparedListener(new OnVideoPreparedListener());

        videoView.setOnCompletionListener(new OnVideoCompletionListener());

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        onVideoReceiver = new OnVideoReceiver();
        registerReceiver(onVideoReceiver,filter);

        onSeekBarChangeListener = new OnVideoSeekBarChangeListener();
        video_volume.setOnSeekBarChangeListener(onSeekBarChangeListener);

        iv_mute.setOnClickListener(this);

        sk_posion.setOnSeekBarChangeListener(onSeekBarChangeListener);
        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new OnVIdeoGestureListener());
        iv_fullscreen.setOnClickListener(this);
    }


    private class OnVIdeoGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            switchControlor();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //双击事件
            switchFullScreen();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            switchPauseStatus();
            super.onLongPress(e);
        }
    }
    /**显示或隐藏控制面板*/
    private void switchControlor() {
        if (isShowControlor){
            //显示状态
            hideControl();
        } else {
            //隐藏状态
            showControlor();
            notifyHideContralor();
        }
    }
    /**显示控制面板*/
    private void showControlor() {
        ViewPropertyAnimator.animate(ll_top).translationY(0);
        ViewPropertyAnimator.animate(ll_bottom).translationY(0);
        isShowControlor = true;

    }
    /**隐藏控制面板*/
    private void hideControl() {
        ViewPropertyAnimator.animate(ll_top).translationY(-ll_top.getHeight());
        ViewPropertyAnimator.animate(ll_bottom).translationY(ll_bottom.getHeight());
        isShowControlor = false;

    }

    /**视频播放完成进行监听*/
    private class OnVideoCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //视频播放结束

            //为了避开系统错误,播放技术后主动更新一下已经播放时间
            mHandler.removeMessages(MSG_UPDATE_POSION);
            updatePosion(videoView.getDuration());

            //更新已经播放的按钮
            updatePauseBtn();

        }
    }

    private class OnVideoSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        /**当进度值发生变更的时候被回调*/
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                //如果不是用户修改就不改变音量值得修改
                return;
            }
            switch (seekBar.getId()){
                case R.id.video_player_sk_volume:
                    updateVolume(progress);
                    break;
                case R.id.video_player_sk_posion:
                    videoView.seekTo(progress);
                    updatePosion(progress);
                    break;
            }

        }
        /*手指压倒seekbar上时回调*/
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(MSG_HIDE_CONTROLOR);
        }
        /**当手指离开seekbar的时候回调*/
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            notifyHideContralor();
        }
    }
    /**通知隐藏控制显示面板*/
    private boolean notifyHideContralor() {
        return mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLOR,5000);
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
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void initData() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            //从外部调用
            videoView.setVideoURI(uri);
            tv_title.setText(uri.getPath());
            iv_next.setEnabled(false);
            iv_pre.setEnabled(false);
        } else {
            //从内部调用
            mVideoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra("videoItems");
            mPosition = getIntent().getIntExtra("position",-1);
            playItem();
        }

        //系统自带进度条
        //videoView.setMediaController(new MediaController(this));

        startUpdateSystemTime();

        //初始化音量
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int currentVolume = getCurrentVolume();
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        video_volume.setMax(maxVolume);
        video_volume.setProgress(currentVolume);

        moveAlpha(0.3f);


        //隐藏控制面板
        initHideControl();


    }
    /*初始化的时候将控制面板隐藏*/
    private void initHideControl() {
        //使用getMeasureHeight 获取高度
        ll_top.measure(0,0);
        int h = ll_top.getMeasuredHeight();
        ViewPropertyAnimator.animate(ll_top).translationY(-ll_top.getMeasuredHeight());

        //LogUtils.e(getClass()," HH  " +h);
        ll_bottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_bottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewPropertyAnimator.animate(ll_bottom).translationY(ll_bottom.getHeight());
            }
        });

        isShowControlor = false;

    }


    /**更新系统时间,并延迟一段时间之后再次更新*/
    private void startUpdateSystemTime() {
        tv_system_time.setText(StringUtils.formatSystemTime());
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SYSTEM_TIME,500);
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.video_playerview_vi_pause:
                switchPauseStatus();
                break;
            case  R.id.video_player_iv_mute:
                switchMuteStatus();
                break;
            case R.id.video_player_iv_pre:
                playPre();
                break;
            case R.id.video_player_iv_next:
                playNext();
                break;
            case R.id.video_player_iv_fullscreen:
                switchFullScreen();
                break;
        }

    }

    /**全屏的切换的默认比例*/
    private void switchFullScreen() {
        updateFullScreenBtn();
        videoView.switchFullscreen();
    }

    /**更新全屏按钮切换的图片*/
    private void updateFullScreenBtn() {
        if (videoView.isFullScreen()) {
            iv_fullscreen.setImageResource(R.drawable.video_fullscreen_selector);
        } else {
            iv_fullscreen.setImageResource(R.drawable.video_defaultscreen_selector);
        }
    }

    /*播放下一个视频*/
    private void playPre() {
        if (mPosition != 0) {
            mPosition--;
            playItem();
        }
    }


    /*播放上一个视频*/
    private void playNext() {
        if (mPosition != mVideoItems.size() - 1) {
            mPosition++;
            playItem();
        }
    }

    /**更新到前一个视频和后一个视频时候的使用的图片*/
    private void updatePreAndNextBtn() {
        iv_pre.setEnabled(mPosition != 0);
        iv_next.setEnabled(mPosition != mVideoItems.size()-1);

    }

    /*播放当前mPosion 选中的视频*/
    private void playItem() {
        if (mVideoItems == null || mVideoItems.size() ==0 || mPosition ==-1){
            return;
        }

//        VideoItem videoItem = (VideoItem) getIntent().getSerializableExtra("videoItem");
        // LogUtils.e(getClass(),""+videoItem);

        VideoItem videoItem = mVideoItems.get(mPosition);
        videoView.setVideoPath(videoItem.getPath());
        tv_title.setText(videoItem.getTitle());
        updatePreAndNextBtn();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(MSG_HIDE_CONTROLOR);
                //记录手指压下去的数据
                mStartY = event.getY();
                mStartVolume = getCurrentVolume();
                mStartAlpha = ViewHelper.getAlpha(alpha_cover);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                //计算手指滑动的距离
                float offSetY = moveY - mStartY;
                //计算手指划过屏幕的百分比
                int helfScreenH = getWindowManager().getDefaultDisplay().getHeight() /2;
                int helfScreenW = getWindowManager().getDefaultDisplay().getWidth() /2;
                float movePercent = offSetY / helfScreenH;

                //在屏幕的左半步修改显示 在屏幕的有半侧修改音量值
                if (event.getX()< helfScreenW){
                    //修改屏幕的亮度
                    moveAlpha(movePercent);
                } else {
                    //修改音量键
                    moveVolume(movePercent);

                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLOR,5000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void moveAlpha(float movePercent) {

        float finalAlpha = mStartAlpha + movePercent;
        if (finalAlpha > 1) {
            finalAlpha = 1;
        } else if (finalAlpha <0) {
            finalAlpha = 0;
        }
        ViewHelper.setAlpha(alpha_cover,finalAlpha);

    }

    /*根据手指划过的距离修改音量键*/
    private void moveVolume(float movePercent) {
        //计算要变化的音量
        int offsetVolue = (int) (video_volume.getMax() * movePercent);
        //计算最终要设置的音量
        int finalVolume = mStartVolume +offsetVolue;
        if (finalVolume> video_volume.getMax()) {
            finalVolume = video_volume.getMax();
        } else if (finalVolume < 0){
            finalVolume = 0;
        }
        updateVolume(finalVolume);
    }

    /**更新声音的状态 如果音量不为0  则记录当前的音量 ,如果为0 则恢复音量之前的记录值*/
    private void switchMuteStatus() {
        if (getCurrentVolume() != 0) {
            //非静音状态
            mCurrentVolume = getCurrentVolume();
            iv_mute.setImageResource(R.drawable.video_mute_selector);
            updateVolume(0);
        } else {
            //静音状态
            updateVolume(mCurrentVolume);
        }

    }

    /**获取当前系统的音量*/
    private int getCurrentVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**更新当前StreamVolume的音量为volume,并且更新音量控制条*/
    private void updateVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,0);
        video_volume.setProgress(volume);
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
            startUpdatePosion();
        } else {
            iv_pause.setImageResource(R.drawable.video_play_selector);
            mHandler.removeMessages(MSG_UPDATE_POSION);
        }

    }

    private class OnVideoPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
            updatePauseBtn();

            //更新播放进度

            int duration = videoView.getDuration();
            tv_duration.setText(StringUtils.formatDuration(duration));
            sk_posion.setMax(duration);
            startUpdatePosion();

        }
    }
    /*更新已经播放的时间*/
    private void startUpdatePosion() {
        int position = videoView.getCurrentPosition();
        updatePosion(position);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSION,500);
    }

    private void updatePosion(int position) {
        tv_posion.setText(StringUtils.formatDuration(position));
        sk_posion.setProgress(position);
    }



}
