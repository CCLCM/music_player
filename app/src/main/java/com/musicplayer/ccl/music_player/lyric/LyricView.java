package com.musicplayer.ccl.music_player.lyric;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.musicplayer.ccl.music_player.R;
import com.musicplayer.ccl.music_player.bean.Lyrics;

import java.util.ArrayList;

/**
 * Created by ccl on 18-2-5.
 */

public class LyricView  extends AppCompatTextView {

    private float hightTextSize;
    private float normaTextSize;
    private int hightColor;
    private int normalColor;
    private Paint mPant;
    private int mHalfViewW;
    private int mHalfViewH;
    private ArrayList<Lyrics> lyrics;
    private int currentLine;
    private int lineHeight;
    private int mDuration;
    private int mPosition;

    public LyricView(Context context) {
        super(context);
        initView();
        
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        hightTextSize = getResources().getDimension(R.dimen.lyric_highlight_size);
        normaTextSize = getResources().getDimension(R.dimen.lyric_normal_size);
        lineHeight = getResources().getDimensionPixelSize(R.dimen.lyric_line_height);
        hightColor = Color.GREEN;
        normalColor = Color.WHITE;
        mPant = new Paint();
        mPant.setAntiAlias(true);
        mPant.setTextSize(hightTextSize);
        mPant.setColor(hightColor);

        //伪造数据
        lyrics = new ArrayList<>();
        for (int i = 0;i<30;i++){
            lyrics.add(new Lyrics(i*2000,"当前歌词的行数"+i));
        }

        currentLine = 5;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfViewW = w / 2;
        mHalfViewH = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawSingLinText(canvas);
        drawMulitLinText(canvas);
    }

    private void drawMulitLinText(Canvas canvas) {
        Lyrics lyric = lyrics.get(currentLine);

        int lineTime;
        if (currentLine != lyrics.size() -1){
            Lyrics nextLyric = lyrics.get(currentLine+1);
            //不是最后一行的时候
            lineTime= nextLyric.getStartPoint() - lyric.getStartPoint();
        } else {
            //最后一行的时候
            lineTime = mDuration - lyric.getStartPoint();
        }

        int passTime = mPosition -lyric.getStartPoint();
        float pastPrencent = passTime /(float)lineTime;
        int offsetY = (int) (pastPrencent *lineHeight);

        Rect bound = new Rect();
        mPant.getTextBounds(lyric.getContent(),0,lyric.getContent().length(),bound);
        int halfTextH = bound.height() / 2;
        int centerY = mHalfViewH + halfTextH - offsetY;
        for (int i = 0;i<lyrics.size();i++){
            if (i == currentLine) {
                mPant.setTextSize(hightTextSize);
                mPant.setColor(hightColor);
            }
            else {
                mPant.setTextSize(normaTextSize);
                mPant.setColor(normalColor);
            }
            // 绘制行的Y= 居中的Y位置 + (要绘制的行 - 居中行) * 行高
            int drawY= centerY + (i - currentLine) * lineHeight;
            drawHrizotalText(canvas,lyrics.get(i).getContent(),drawY);
        }

    }

    private void drawSingLinText(Canvas canvas) {
        String  text = "正在加载歌词";
        Rect bound = new Rect();
        mPant.getTextBounds(text,0,text.length(),bound);
        int halfTextH = bound.height() / 2;
        int drawY = mHalfViewH + halfTextH;

        drawHrizotalText(canvas, text,drawY);
    }

    /**绘制横向文本的位置*/
    private void drawHrizotalText(Canvas canvas, String text, int drawY) {
        int halfTextW = (int) (mPant.measureText(text,0,text.length()) / 2);
        int drawX= mHalfViewW -halfTextW;
        canvas.drawText(text,drawX,drawY,mPant);
    }

    /**根据播放到position的时间 选择高亮的歌词*/
    public void rool(int position,int duration) {
        mPosition = position;
        mDuration = duration;
        //高亮的行为:起始时间小于position ,且下一行开始的时间大于postion
        for (int i = 0;i < lyrics.size(); i++) {
            Lyrics lyric = lyrics.get(i);
            int nextTime = 0;
            if (i != lyrics.size() -1) {
                Lyrics nextLyric = lyrics.get(i+1);
                nextTime = nextLyric.getStartPoint();
            } else {
                //最后一行
                nextTime = duration;
            }
            if (lyric.getStartPoint() <= position && nextTime > position){
                currentLine = i;
                break;
            }
        }
        invalidate();
    }
}
