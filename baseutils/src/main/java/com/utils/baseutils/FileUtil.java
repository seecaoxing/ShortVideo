package com.utils.baseutils;

import android.content.Context;
import android.os.Environment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by li_bin on 2016/11/2.
 * 文件操作帮助类
 */

public class FileUtil {

    public static String getCacheImagePath(Context context) {
        File file = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        return file.getAbsolutePath();
    }

    public static String getPermanentImagePath(Context context) {
        File imgDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (imgDir.exists() && imgDir.isFile()) {
            imgDir.delete();
        }
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        File file = new File(imgDir, System.currentTimeMillis() + ".jpg");
        return file.getAbsolutePath();
    }

    public static void copyFile(String from, String to) {
        try {
            int byteread;
            File oldfile = new File(from);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(from);
                FileOutputStream fs = new FileOutputStream(to);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
