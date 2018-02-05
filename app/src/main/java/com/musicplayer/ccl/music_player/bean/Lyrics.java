package com.musicplayer.ccl.music_player.bean;

/**
 * Created by ccl on 18-2-5.
 */

public class Lyrics {
    private int startPoint; //歌词的起始时间
    private String content;//歌词的正文

    public Lyrics(int startPoint, String content) {
        this.startPoint = startPoint;
        this.content = content;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "startPoint=" + startPoint +
                ", content='" + content + '\'' +
                '}';
    }
}
