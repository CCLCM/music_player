package com.musicplayer.ccl.music_player.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.musicplayer.ccl.music_player.R;

import utils.LogUtils;

/**
 * Created by ccl on 18-1-30.
 * 1.规范代码结构
 * 2.提供公用的方法
 * 3
 */

public abstract class  BaseActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layouId());
        initView();
        initListener();
        initData();
        registerCommonBtn();
    }
    /**多个界面在此处点击注册*/
    protected  void registerCommonBtn(){
        View view = findViewById(R.id.back);
        if (view != null){
            view.setOnClickListener(this);
        }

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

    protected abstract void processClick( View view);
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

    public  void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public  void toast(int msgId) {
        Toast.makeText(this,msgId,Toast.LENGTH_SHORT).show();
    }
    /**显示一个error等级的log*/
    public  void logE(String log) {
        LogUtils.e(getClass(),log);
    }




}
