package com.musicplayer.ccl.music_player.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.musicplayer.ccl.music_player.R;

import utils.LogUtils;

/**
 * Created by ccl on 18-1-31.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),getLayoutId(),null);
        initView(view);
        initListener();
        initData();
        registerCommonBtn(view);
        return view;
    }


    /**多个界面在此处点击注册
     * @param root*/
    protected  void registerCommonBtn(View root){
        View view = root.findViewById(R.id.back);
        if (view != null){
            view.setOnClickListener(this);
        }

    }

    /**
     * 返回当前的activity布局id
     * @return
     */
    /**执行findviewbyid 操作*/
    protected abstract int getLayoutId();
    /**初始化方法*/
    protected abstract void initView(View view);
    /**祖册监听器*/
    protected abstract void initListener();
    /**初始数据*/
    protected abstract void initData();

    protected abstract void processClick(View view);
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            default:
                processClick(view);
        }

    }

    public  void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    public  void toast(int msgId) {
        Toast.makeText(getActivity(),msgId,Toast.LENGTH_SHORT).show();
    }
    /**显示一个error等级的log*/
    public  void logE(String log) {
        LogUtils.e(getClass(),log);
    }
}
