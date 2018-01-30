package com.musicplayer.ccl.music_player.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.musicplayer.ccl.music_player.R;

/**
 * Created by ccl on 18-1-30.
 * 1.规范代码结构
 * 2.提供公用的方法
 * 3
 */

public abstract class  BaseActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layouId());
        initView();
        initListener();
        initData();
    }

    /**
     * 返回当前的activity布局id
     * @return
     */
    /**执行findviewbyid 操作*/
    protected abstract int layouId();
    /**初始化方法*/
    protected abstract void initView();
    /**祖册监听器*/
    protected abstract void initListener();
    /**初始数据*/
    protected abstract void initData();

    protected abstract void processClick(View view);
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                processClick(view);
        }

    }

}
