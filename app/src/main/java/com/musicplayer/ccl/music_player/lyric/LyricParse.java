package com.musicplayer.ccl.music_player.lyric;

import android.support.annotation.NonNull;

import com.musicplayer.ccl.music_player.bean.Lyrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ccl on 18-2-5.
 */

public class LyricParse{
    /**从解析文件解析出歌词*/
    public static ArrayList<Lyrics> paseFromFile(File lyricFile){
        ArrayList<Lyrics> lyrics = new ArrayList<>();
        if (lyricFile == null || !lyricFile.exists()) {
            lyrics.add(new Lyrics(0,"找不到歌词"));
            return lyrics;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(lyricFile),"utf-8"));
            String line = bufferedReader.readLine();
            while (line != null) {

                ArrayList<Lyrics> linelist = paseLine(line);
                lyrics.addAll(linelist);
                //解析一行歌词
                line = bufferedReader.readLine();
            }


        } catch (Exception e) { e.printStackTrace();
        }

        //由于可能在一行歌词中有多个时间点,列表 需要排序
        Collections.sort(lyrics);
        return  lyrics;
    }

    /**解析一行歌词 */
    private static ArrayList<Lyrics> paseLine(String line) {

        ArrayList<Lyrics> linlist = new ArrayList<>();

        String[] arr = line.split("]");
        String content = arr[arr.length-1];

        for (int i=0;i<arr.length -1;i++)
        {
            int startPoint = parseTime(arr[i]);
            Lyrics lyri = new Lyrics(startPoint,content);
            linlist.add(lyri);
        }

        return linlist;


    }

    private static int parseTime(String timeStr) {
        int mstartPoint = 0;
        String[] arr = timeStr.split(":");
        String minStr = arr[0].substring(1,arr[0].length());
        arr = arr[1].split("\\.");
        String secStr = arr[0];
        String mSecstr = arr[1];
        int min = Integer.parseInt(minStr);
        int sec = Integer.parseInt(secStr);
        int mSec = Integer.parseInt(mSecstr);

        mstartPoint = min * 60 *1000 + sec * 1000 + mSec * 10;
        return mstartPoint;
    }

}
