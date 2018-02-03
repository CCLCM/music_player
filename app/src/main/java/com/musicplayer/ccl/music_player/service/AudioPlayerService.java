package com.musicplayer.ccl.music_player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

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

    }

}
