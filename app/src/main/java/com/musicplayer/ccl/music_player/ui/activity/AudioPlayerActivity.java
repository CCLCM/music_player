package com.musicplayer.ccl.music_player.ui.activity;

import android.media.MediaPlayer;
import android.view.View;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.AudioItem;
import com.musicplayer.ccl.music_player.ui.activity.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class AudioPlayerActivity extends BaseActivity {

    private ArrayList<AudioItem> audioItems;
    private int mPostion;

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

        audioItems = (ArrayList<AudioItem>) getIntent().getSerializableExtra("audioItems");
        mPostion = getIntent().getIntExtra("postion",-1);
        AudioItem audioItem = audioItems.get(mPostion);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioItem.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processClick(View view) {

    }
}
