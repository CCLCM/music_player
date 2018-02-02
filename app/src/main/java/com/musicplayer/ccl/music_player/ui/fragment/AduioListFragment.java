package com.musicplayer.ccl.music_player.ui.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.ad.MobileAsyncQuerayHandler;
import com.musicplayer.ccl.music_player.adapter.AudioListAdapter;
import com.musicplayer.ccl.music_player.bean.AudioItem;
import com.musicplayer.ccl.music_player.ui.activity.AudioPlayerActivity;

import java.util.ArrayList;

/**
 * Created by ccl on 18-1-31.
 */

public class AduioListFragment extends BaseFragment {

    private ListView listView;
    private AudioListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.main_audio_list;
    }

    @Override
    protected void initView(View view) {
        listView = view.findViewById(R.id.simple_listview);

    }

    @Override
    protected void initListener() {
        mAdapter = new AudioListAdapter(getActivity(),null);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnAudioItemClickListener());
    }


    private class OnAudioItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取到被点击的数据
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            //挑传到播放界面
            Intent intent = new Intent(getActivity(),AudioPlayerActivity.class);
            ArrayList<AudioItem> audioItems = AudioItem.instanceListFromCursor(cursor);
            intent.putExtra("audioItems",audioItems);
            intent.putExtra("postion",position);
            startActivity(intent);
        }
    }

    @Override
    protected void initData() {
        //从mediaprovider获取数据
        ContentResolver resolver = getActivity().getContentResolver();
//        Cursor cursor =resolver.query(Media.EXTERNAL_CONTENT_URI,new String[]{Media._ID,Media.DATA,Media.DISPLAY_NAME,Media.ARTIST},null,null,null);
//        //CursorUtils.printCursor(cursor);
//        mAdapter.swapCursor(cursor);

        MobileAsyncQuerayHandler asyncQuerayHandler = new MobileAsyncQuerayHandler(resolver);
        asyncQuerayHandler.startQuery(1,mAdapter,Media.EXTERNAL_CONTENT_URI,new String[]{Media._ID,Media.DATA,Media.DISPLAY_NAME,Media.ARTIST},null,null,null);
    }

    @Override
    protected void processClick(View view) {

    }

}
