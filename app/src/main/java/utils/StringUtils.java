package utils;

/**
 * Created by ccl on 18-2-1.
 */

public class StringUtils {
    private static final int HOUR = 60 * 60 * 1000;
    private static final int MIN = 60 * 1000;
    private static final int SEC = 1000;

    /**
     * 格式时间为 01:01:01  或者是 01:01
     */
    public static String formatDuration(int duration) {
        String time = null;

        int hour = duration / HOUR;
        int min = duration % HOUR / MIN;
        int sec = duration % MIN / SEC;
        if (hour ==0) {
            // 01:01
            time = String.format("%02d:%02d",min,sec);
        } else {
            // 01:01:01
            time = String.format("%02d:%02d:%02d",hour,min,sec);
        }

        return time;

    }
}
