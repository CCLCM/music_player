package com.musicplayer.ccl.music_player.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;


import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Video.Media;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.ad.MobileAsyncQuerayHandler;
import com.musicplayer.ccl.music_player.adapter.VideoListAdapter;
import com.musicplayer.ccl.music_player.bean.VideoItem;
import com.musicplayer.ccl.music_player.ui.activity.VideoPlayerActivity;

/**
 * Created by ccl on 18-1-31.
 */

public class AudioListFragment extends BaseFragment {

    private ListView listView;
    private Cursor cursor;
    private VideoListAdapter videoListAdapter;

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
        videoListAdapter = new VideoListAdapter(getActivity(),null);
        listView.setAdapter(videoListAdapter);
        listView.setOnItemClickListener(new OnVideoItemClickListener());
    }

    private class OnVideoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //获取被点击的数据
             Cursor mcursor = (Cursor) adapterView.getItemAtPosition(i);
            VideoItem videoItem =VideoItem.instanceFromCursor(mcursor);

            //跳转到播放界面

            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("videoItem",videoItem);
            startActivity(intent);

        }
    }

    @Override
    protected void initData() {
            ContentResolver resolver = getActivity().getContentResolver();

            //开启主线程查询
//            cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{Media._ID,Media.DATA, Media.TITLE, Media.SIZE, Media.DURATION}, null, null, null);
//            if (cursor !=null) {
//                CursorUtils.printCursor(cursor);
//            }
//        videoListAdapter.swapCursor(cursor);

        //开启子线程查询
        AsyncQueryHandler asyncQueryHandler = new MobileAsyncQuerayHandler(resolver);
        asyncQueryHandler.startQuery(0,videoListAdapter,Media.EXTERNAL_CONTENT_URI, new String[]{Media._ID,Media.DATA, Media.TITLE, Media.SIZE, Media.DURATION}, null, null, null);

    }



    @Override
    protected void processClick(View view) {

    }


}
