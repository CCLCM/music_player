package com.musicplayer.ccl.music_player.bean;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;

import static android.provider.MediaStore.Video.Media;

/**
 * Created by ccl on 18-1-31.
 */

public class AudioItem implements Serializable {
    private String title;
    private String arties;

    public String getArties() {
        return arties;
    }

    public void setArties(String arties) {
        this.arties = arties;
    }

    private String path;

    public static AudioItem instanceFromCursor(Cursor cursor){
        AudioItem viewItem = new AudioItem();
        if (cursor ==null||cursor.getCount() ==0){
            return viewItem;
        }
        viewItem.title = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
        viewItem.arties = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
        viewItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        return viewItem;
    }
    /**从cosor中查询所有的视频*/
    public static ArrayList<AudioItem> instanceListFromCursor(Cursor cursor) {
        ArrayList<AudioItem> audioItems = new ArrayList<>();
        if (cursor ==null || cursor.getCount() ==0) {
            return audioItems;
        }

        cursor.moveToPosition(-1); //将头指针移动到列表的最前面
        while (cursor.moveToNext()){
            AudioItem videoItem = instanceFromCursor(cursor);
            audioItems.add(videoItem);
        }

        return audioItems;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AudioItem{" +
                "title='" + title + '\'' +
                ", arties='" + arties + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
