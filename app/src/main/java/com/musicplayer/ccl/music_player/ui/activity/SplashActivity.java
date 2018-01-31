package com.musicplayer.ccl.music_player.ui.activity;

import android.content.Intent;
import android.view.View;

import com.musicplayer.ccl.music_player.R;

import java.util.logging.Handler;

/**
 * Created by ccl on 18-1-30.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected int layouId() {
        return R.layout.splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //延时跳转到主页面
        //1.创建一个子现场,休眠一段时间后跳转
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        }.start();


        //2.Handler发送延迟消息

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}
