package com.musicplayer.ccl.music_player.ui.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.Media;
import android.view.View;
import android.widget.ListView;

import com.musicplayer.ccl.music_player.R;

import utils.CheckPermissionUtils;
import utils.CursorUtils;
import utils.LogUtils;

/**
 * Created by ccl on 18-1-31.
 */

public class AudioListFragment extends BaseFragment {

    private ListView listView;
    private Cursor cursor;

    @Override
    protected int getLayoutId() {
        return R.layout.main_audio_list;
    }

    @Override
    protected void initView(View view) {

        listView = (ListView) view.findViewById(R.id.simple_listview);


    }

    @Override
    protected void initListener() {
//        listView.setAdapter(void);.
    }

    @Override
    protected void initData() {
        //从MediaProvider获取数据

        if (CheckPermissionUtils.verifyStoragePermissions(getActivity())) {
            ContentResolver resolver = getActivity().getContentResolver();
            cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.DATA, Media.TITLE, Media.SIZE, Media.DURATION}, null, null, null);
            CursorUtils.printCursor(cursor);
        }

    }



    @Override
    protected void processClick(View view) {

    }
}
