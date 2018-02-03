package com.musicplayer.ccl.music_player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        audioItems = (ArrayList<AudioItem>) intent.getSerializableExtra("audioItems");
        mPostion = intent.getIntExtra("postion",-1);
        AudioItem audioItem = audioItems.get(mPostion);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioItem.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
