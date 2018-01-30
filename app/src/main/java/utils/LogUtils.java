package utils;

import android.util.Log;

/**
 * Created by ccl on 18-1-30.
 */

public class LogUtils {

    public static final  boolean ENABLE = true;

    public static void d(String tag ,String msg){
        if (ENABLE){
            Log.d("chencl_"+tag,msg);
        }
    }


    public static void e(String tag ,String msg){
        if (ENABLE){
            Log.e("chencl_"+tag,msg);
        }
    }


    public static void e(Class tag ,String msg){
        if (ENABLE){
            Log.e("chencl_"+tag.getSimpleName(),msg);
        }
    }

}
