package com.musicplayer.ccl.music_player.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.VideoItem;

import utils.StringUtils;

/**
 * Created by ccl on 18-1-31.
 */

public class VideoListAdapter extends CursorAdapter {
    public VideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public VideoListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public VideoListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //创建新的view
        View view = View.inflate(context, R.layout.main_audio_list_item,null);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //填充view
        ViewHolder holder = (ViewHolder) view.getTag();
        VideoItem videoItem = VideoItem.instanceFromCursor(cursor);

        holder.tv_title.setText(videoItem.getTitle());
        holder.tv_duration.setText(StringUtils.formatDuration(videoItem.getDuration()));
        holder.tv_size.setText(Formatter.formatFileSize(context,videoItem.getSize()));

    }

    private  class ViewHolder{
        TextView tv_title,tv_duration,tv_size;
        ViewHolder(View view) {
        tv_title = (TextView) view.findViewById(R.id.main_video_item_tv_title);
        tv_duration = (TextView) view.findViewById(R.id.main_video_item_tv_duration);
        tv_size = (TextView) view.findViewById(R.id.main_video_item_tv_size);
        }
    }
}
