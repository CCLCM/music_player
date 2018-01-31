package com.musicplayer.ccl.music_player.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ccl on 18-1-31.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MainPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
