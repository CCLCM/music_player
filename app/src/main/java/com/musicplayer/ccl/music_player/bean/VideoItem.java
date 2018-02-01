package com.musicplayer.ccl.music_player.bean;

import android.database.Cursor;

import java.io.Serializable;

import static android.provider.MediaStore.Video.Media;

/**
 * Created by ccl on 18-1-31.
 */

public class VideoItem implements Serializable {
    private String title;
    private int duration;
    private int size;
    private String path;

    public static VideoItem instanceFromCursor(Cursor cursor){
        VideoItem viewItem = new VideoItem();
        if (cursor ==null||cursor.getCount() ==0){
            return viewItem;
        }
        viewItem.title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
        viewItem.duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION));
        viewItem.size = cursor.getInt(cursor.getColumnIndex(Media.SIZE));
        viewItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        return viewItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }
}
