package utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by ccl on 18-1-31.
 */

public class CheckPermissionUtils {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static boolean verifyStoragePermissions(Activity activity) {
        boolean persion = false;
        try {
            //检测是否有写的权限
            int permission_write = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            //检测是否有写的权限
            int permission_read = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission_write != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

            }

            if (permission_read != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }

            if ((ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE") == 0) |  (ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE") == 0)){
                 persion = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return persion;
    }
}
