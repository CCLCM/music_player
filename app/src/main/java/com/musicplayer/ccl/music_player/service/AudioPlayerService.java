package com.musicplayer.ccl.music_player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.musicplayer.ccl.music_player.bean.AudioItem;

import java.io.IOException;
import java.util.ArrayList;

import utils.LogUtils;

/**
 * Created by ccl on 18-2-3.
 */

public class AudioPlayerService extends Service {
    private ArrayList<AudioItem>  audioItems;
    private int mPostion;
    private  AudioServiceBinder audioServiceBinder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioServiceBinder = new AudioServiceBinder();
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

        private MediaPlayer mediaPlayer;

        public void play(){
            AudioItem audioItem = audioItems.get(mPostion);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioItem.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void pause(){
            mediaPlayer.pause();
        }
        public void start(){
            mediaPlayer.start();
        }

        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }
    }

}
