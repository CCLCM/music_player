package com.musicplayer.ccl.music_player.ad;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class MobileAsyncQuerayHandler extends AsyncQueryHandler {
    public MobileAsyncQuerayHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        CursorAdapter adapter = (CursorAdapter) cookie;
        adapter.swapCursor(cursor);
    }
}
