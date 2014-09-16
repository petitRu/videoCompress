package com.examples.ffmpeg4android_demo.util;

import java.io.File;
import android.os.Environment;



/**
 * Created by a206208 on 15/09/2014.
 */

public class AndroidUtil {
    public static File getStoragePath() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

}
