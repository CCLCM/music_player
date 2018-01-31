package com.musicplayer.ccl.music_player.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.adapter.MainPagerAdapter;
import com.musicplayer.ccl.music_player.ui.fragment.MusicListFragment;
import com.musicplayer.ccl.music_player.ui.fragment.AudioListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private MainPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layouId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.mian_viewpager);

    }

    @Override
    protected void initListener() {
        mFragments = new ArrayList<>();
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mFragments.add(new AudioListFragment());
        mFragments.add(new MusicListFragment());
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void processClick(View view) {

    }
}
