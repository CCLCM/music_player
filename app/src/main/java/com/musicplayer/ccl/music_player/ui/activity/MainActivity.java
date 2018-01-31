package com.musicplayer.ccl.music_player.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.adapter.MainPagerAdapter;
import com.musicplayer.ccl.music_player.ui.fragment.MusicListFragment;
import com.musicplayer.ccl.music_player.ui.fragment.AudioListFragment;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private MainPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private TextView tv_video;
    private TextView tv_audio;
    private View mainIndicateLine;
    private int screenWidth;
    private AudioListFragment audioListFragment;
    private MusicListFragment musicListFragment;

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
        tv_video = (TextView) findViewById(R.id.mian_tv_video);
        tv_audio = (TextView) findViewById(R.id.mian_tv_audio);
        mainIndicateLine = findViewById(R.id.main_indicate_line);

    }

    @Override
    protected void initListener() {
        mFragments = new ArrayList<>();
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new OnVideoPageChangeListener());
        tv_video.setOnClickListener(this);
        tv_audio.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        audioListFragment = new AudioListFragment();
        musicListFragment = new MusicListFragment();
        mFragments.add(audioListFragment);
        mFragments.add(musicListFragment);
        mAdapter.notifyDataSetChanged();
        updateTabs(0);

        //初始化指示器的屏幕宽度的一半

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mainIndicateLine.getLayoutParams().width = screenWidth /2;
       // mainIndicateLine.invalidate();
        mainIndicateLine.requestLayout();




    }


    @Override
    protected void processClick(View view) {

        switch (view.getId()) {

            case R.id.mian_tv_video:
                viewPager.setCurrentItem(0);
                break;
            case R.id.mian_tv_audio:
                viewPager.setCurrentItem(1);
                break;
        }

    }

    /**
     * 当viewpager发生变化的时候会回调此方法
     *
     * */
    private class OnVideoPageChangeListener implements ViewPager.OnPageChangeListener {
        /**当page滑动的时候回调此方法*/
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            LogUtils.e(getClass(),"position " +position+ "   positionOffset  " +positionOffset + "   positionOffsetPixels " +positionOffsetPixels) ;
                //移动距离 = 起始位置 + 偏移大小
                // 起始位置 = posion  * 指示器宽度
                // 偏移大小 = 手指划过屏幕的百分百 * 指示器宽度
                int offsetX = (int) (positionOffset * mainIndicateLine.getWidth());
                int startX = position * mainIndicateLine.getWidth();
                int translationX = startX + offsetX;

            ViewHelper.setTranslationX(mainIndicateLine,translationX);

        }
        /**当page被选中的时候回调此方法*/
        @Override
        public void onPageSelected(int position) {
            updateTabs(position);

        }


        /**当pager的滑动发生变化时候调用*/
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void updateTabs(int position) {
        updateTab(position, 0, tv_video);
        updateTab(position, 1, tv_audio);
    }


    /**根据tab是否等于position 修改颜色和大小*/
    private void updateTab(int position, int tabposion, TextView tab) {
        int green = getResources().getColor(R.color.green);
        int halfwhite = getResources().getColor(R.color.halfwhite);
        if (position == tabposion) {
            tab.setTextColor(green);
            ViewPropertyAnimator.animate(tab).scaleX(1.2f).scaleY(1.2f);
            //tab.setTextSize();
        } else {
            tab.setTextColor(halfwhite);
            ViewPropertyAnimator.animate(tab).scaleX(1.0f).scaleY(1.0f);
        }
    }
}
