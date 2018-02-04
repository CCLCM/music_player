package com.musicplayer.ccl.music_player.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.musicplayer.ccl.music_player.bean.AudioItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import utils.LogUtils;

/**
 * Created by ccl on 18-2-3.
 */

public class AudioPlayerService extends Service {
    private ArrayList<AudioItem>  audioItems;
    private int mPostion;
    /**播放列表循环*/
    public static final int PLAYMODE_ALL_REPEAT = 0;
    /**播放列表随机*/
    public static final int PLAYMODE_RANDOM = 1;
    /**播放列表单曲*/
    public static final int PLAYMODE_SINGLE_REPEAT = 2;
    private  AudioServiceBinder audioServiceBinder;
    /**播放列表模式*/
    private int mPlayMode = PLAYMODE_ALL_REPEAT;
    private SharedPreferences mPreferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioServiceBinder = new AudioServiceBinder();
        mPreferences = getSharedPreferences("audio.conf",MODE_PRIVATE);
        mPlayMode = mPreferences.getInt("play_id",PLAYMODE_ALL_REPEAT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        audioItems = (ArrayList<AudioItem>) intent.getSerializableExtra("audioItems");
        mPostion = intent.getIntExtra("postion",-1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class AudioServiceBinder extends Binder {

        private class OnAudioPreparedListener implements MediaPlayer.OnPreparedListener {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //音乐资源准备完成准备播放
                mediaPlayer.start();
                //获取当前正在播放的歌曲
                AudioItem  audioitem = audioItems.get(mPostion);
                Intent intent = new Intent("com.chencl.mobileplayer.audio_player");
                intent.putExtra("audioitem",audioitem);
                sendBroadcast(intent);
            }
        }

        /**音乐播放结束的监听器*/
        private class OnVideoCompletionListener implements MediaPlayer.OnCompletionListener {
            @Override
            public void onCompletion(MediaPlayer mp) {

                //歌曲播放结束

                //根据当前播放模式,选择下一首歌曲,并播放

                autoPlayNext();
            }
        }

        private MediaPlayer mediaPlayer;
        /**播放*/
        public void play(){
            AudioItem audioItem = audioItems.get(mPostion);
            /**如果有播放的歌曲则释放歌曲*/
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioItem.getPath());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new OnAudioPreparedListener());
                mediaPlayer.setOnCompletionListener(new OnVideoCompletionListener());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**暂停*/
        public void pause(){
            mediaPlayer.pause();
        }
        /**播放*/
        public void start(){
            mediaPlayer.start();
        }
        /**播放的状态*/
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }
        /**返回当前歌曲的总时长*/
        public int getDuration(){
            return mediaPlayer.getDuration();
        }

        /**返回当前歌曲的播放进度*/
        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }

        public void seekTo(int msec) {
            mediaPlayer.seekTo(msec);
        }
        /**播放上一首歌*/
        public void playPre(){
            if (mPostion != 0) {
                mPostion--;
                play();
            } else {
                Toast.makeText(AudioPlayerService.this,"已经是第一首歌曲",Toast.LENGTH_SHORT).show();
            }

        }
        /**播放下一首歌*/
        public void playNext(){
            if (mPostion != audioItems.size()-1){
                mPostion++;
                play();
            } else {
                Toast.makeText(AudioPlayerService.this,"已经是最后一首歌曲",Toast.LENGTH_SHORT).show();
            }
        }

        /**依次切换播放模式*/
        public void switchPlayMode() {
            switch (mPlayMode) {
                case PLAYMODE_ALL_REPEAT:
                mPlayMode = PLAYMODE_RANDOM;
                break;
                case PLAYMODE_RANDOM:
                mPlayMode = PLAYMODE_SINGLE_REPEAT;
                break;
                case PLAYMODE_SINGLE_REPEAT:
                mPlayMode = PLAYMODE_ALL_REPEAT;
                break;
            }
            mPreferences.edit().putInt("play_id",mPlayMode).commit();
        }
        /**返回当前使用的播放模式*/
        public int getPlayMode(){
            return mPlayMode;
        }

        /**根据当前播放模式,选择下一首歌曲,并播放*/
        private void autoPlayNext() {
            switch (mPlayMode){
                case PLAYMODE_ALL_REPEAT:
                    //播放下一首歌曲,如果说是最后一首则播放第一首歌曲
                    if (mPostion == audioItems.size()-1){
                        mPostion = 0;
                    } else {
                        mPostion++;
                    }
                    break;
                case PLAYMODE_RANDOM:
                    //随机选取一首歌
                    mPostion = new Random().nextInt(audioItems.size());
                    break;
                case PLAYMODE_SINGLE_REPEAT:
                    //单曲循环
                    break;
            }
            //播放选中的歌曲
            play();
        }

    }

}
