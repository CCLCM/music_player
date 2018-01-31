package utils;

import android.database.Cursor;

/**
 * Created by ccl on 18-1-31.
 */

public class CursorUtils {
    private static  final String TAG = "CursorUtils";
    public static void printCursor(Cursor cursor){

        LogUtils.e(TAG,"videolistfragment: 查询到的数据为"+cursor.getCount());

        while (cursor.moveToNext()) {
            LogUtils.e(TAG,"======================================================");
            for (int i =0;i<cursor.getColumnCount();i++){
                LogUtils.e(TAG,"videolistfragment.initdate: name " + cursor.getColumnName(i)+";value="+cursor.getString(i));
            }
        }
    }
}
