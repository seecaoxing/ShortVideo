package com.utils.baseutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 * 网络图片加工处理类
 * 
 * @author zhangyp
 */
public class ImgUtils {

    // 3g图片缩略服务
    public final static String imageResize_Host = "http://s.cimg.163.com/i/";

    // 非wifi情况在图片缩略质量百分比
    public static String quality = ".60";

    /**
     * 3G图片缩略服务URL
     * 

     * @param width
     * @param length
     * @return
     */
    public static String get3gResizeImgUrl(String orgUrl, int width, int length) {

        if (TextUtils.isEmpty(orgUrl)) {
            return "";
        }

        String url = imageResize_Host;

        if (width <= 0 || length <= 0) {
            url = orgUrl;
        } else {
            if (orgUrl.startsWith("http://")) {
                url = url + orgUrl.replace("http://", "") + "." + width + "x" + length + ".auto.jpg";
            } else {
                url = url + orgUrl + "." + width + "x" + length + ".auto.jpg";
            }
        }

        return url;
    }

//    /**
//     * 获得图片缩略URL按照屏幕比例
//     *
//     * @param orgUrl
//     * @param c
//     * @return
//     */
//    public static String getImgUrlForScreen(String orgUrl, Context c) {
//
//        HashMap<String, Integer> m = SystemUtils.getWidth_Height(c);
//        int width = m.get("w");
//        int height = m.get("h");
//        String rst = orgUrl;
//        rst = get3gResizeImgUrl(orgUrl, width, height);
//
//        return rst;
//
//    }

    /**
     * 判断是否为待网络缩略的图片
     * 
     * @param url
     * @return boolean
     */
    public static boolean isResizeUrl(String url) {

        if (null == url || "".equalsIgnoreCase(url)) {
            return false;
        } else {
            if (url.startsWith(imageResize_Host) && url.endsWith(".auto.jpg")) {
                return true;
            } else {
                return false;
            }
        }
    }
    public static Bitmap decodeFile(String filePath, int maxResolution) {
        return decodeFile(filePath, maxResolution, maxResolution, Config.RGB_565);
    }
    public static Bitmap decodeFile(String filePath, int reqHeight, int reqWidth, Config config) {
        if (filePath == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (reqHeight != 0 && reqWidth != 0) {
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            }
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = config;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            int degree = getExifOrientation(filePath);
            return rotateBitmap(bitmap, degree);
        } catch (OutOfMemoryError e) {
        }
        return null;

    }
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {}
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if ((width > height && width < 2 * height) || (height > 2 * width)) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (bitmap == null || degrees == 0) {
            return bitmap;
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return ImgUtils.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        if (width <= 0 || height <= 0) {
            return source;
        }
        try {
            return Bitmap.createBitmap(source, x, y, width, height, m, filter);
        } catch (OutOfMemoryError e) {
            return source;
        } catch (IllegalArgumentException e) {
            return source;
        }
    }
    public static void saveBitmap(Bitmap bitmap, String fileName, int w, int h) {
        Bitmap bmp = zoomBitmap(bitmap, w, h);
        try {

            FileOutputStream os = new FileOutputStream(fileName);
            bmp.compress(CompressFormat.JPEG, 75, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bitmap, String fileName, int quality) {
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            bitmap.compress(CompressFormat.JPEG, quality, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        return createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
    public static Bitmap cropBitmap(Bitmap bitmap, Rect r) {
        int width = r.right - r.left;
        int height = r.bottom - r.top;
        Bitmap bmp = createBitmap(width, height, Config.RGB_565);
        if (bmp == null) {
            return bitmap;
        }
        RectF dst = new RectF(0, 0, width, height);
        Canvas canvas = new Canvas(bmp);
        final Paint paint = new Paint();
        canvas.drawBitmap(bitmap, r, dst, paint);
        return bmp;
    }
    public static Bitmap createBitmap(int width, int height, Config config) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

//    /**
//     * 在非wifi网络条件下 改造图片缩略下载URL，从而可以下载质量较低的图片，极少流量，提高速度
//     *
//     * @param url
//     * @param context
//     * @return String
//     */
//    public static String getImgUrlByNetType(String url, Context context, int width, int height) {
//
//        String tempurl = url;
//
//        if (isResizeUrl(url)) {
//
//            if (!NetUtils.isWifi(context)) {
//                tempurl = url.substring(0, (url.length() - 9)) + quality + ".auto.jpg";
//            }
//
//        } else {
//
//            tempurl = get3gResizeImgUrl(url, width, height);
//
//            if (isResizeUrl(tempurl)) {
//
//                if (!NetUtils.isWifi(context)) {
//                    tempurl = tempurl.substring(0, (tempurl.length() - 9)) + quality + ".auto.jpg";
//                }
//
//            }
//        }
//
//        return tempurl;
//    }

    /**
     * 按w和h等比裁剪图片裁切图片
     * 
     * @param src
     *            原图

     * @return Bitmap
     */
    public static Bitmap ratioCutPic(Bitmap src, int width, int height) {

        Bitmap bitmap = null;

        if (src == null || width == 0 || height == 0) {
            return bitmap;
        }

        try {
            final int srcW = src.getWidth();
            final int srcH = src.getHeight();
            final float sW = ((float) srcW) / width;
            final float sH = ((float) srcH) / height;
            if (sW == 1 && sH == 1) {
                // 需要的尺寸一致,直接返回
                bitmap = src;
            } else if (sW == sH) {
                // 长宽比一致
                bitmap = src;
            } else if (sW > sH) {
                int offsetX = Math.round((srcW - width * sH) / 2);
                int offsetY = 0;
                bitmap = Bitmap.createBitmap(src, offsetX, offsetY, srcW - 2 * offsetX, srcH);
            } else {
                int offsetX = 0;
                int offsetY = Math.round((srcH - height * sW) / 2);
                bitmap = Bitmap.createBitmap(src, offsetX, offsetY, srcW, srcH - 2 * offsetY);
            }
        } catch (Exception e) {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        } finally {
            if (src != null && src != bitmap) {
                src.recycle();
            }
        }

        return bitmap;
    }

    /**
     * 按固定比例比例进行裁剪
     * 
     * @param context
     * @param src
     * @param frameW
     * @param frameH
     * @return
     */
    public static Bitmap resizeBitmap(Context context, Bitmap src, final int frameW, final int frameH) {

        if (src == null) {
            return null;
        }

        final int w = src.getWidth();// 源文件的大小
        final int h = src.getHeight();
        if (w == 0 || h == 0)
            return null;

        if (w == frameW && h == frameH) {// 如果比例相同，那么不进行缩略
            return src;
        } else {
            try {
                float scaleWidth = ((float) frameW) / w;// 宽度缩小比例
                float scaleHeight = ((float) frameH) / h;// 高度缩小比例

                Matrix matrix = new Matrix();// 矩阵
                matrix.postScale(scaleWidth, scaleHeight);// 设置矩阵比例

                Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);// 直接按照矩阵的比例把源文件画入进行

                if (src != null && !src.isRecycled()) {
                    src.recycle();
                    src = null;
                }

                return resizedBitmap;
            } catch (OutOfMemoryError e) {

            }
            return null;
        }

    }

    public static Bitmap fastblurJava(Bitmap inBitmap, int radius, float aspectRatio) {
        return fastblurJava(inBitmap, radius, aspectRatio, 1);
    }

    // java 虚化方式
    /**
     * @param inBitmap
     *            输入图片
     * @param ratio
     *            压缩率
     * @param radius
     *            虚化半径
     * @return bitmap
     */
    public static Bitmap fastblurJava(Bitmap inBitmap, int radius, float aspectRatio, int ratio) throws OutOfMemoryError {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        if (radius < 1) {
            return inBitmap;
        }
        
        Bitmap bitmap = zoomBitmap(inBitmap, ratio);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        int targetWidth = w;
        int targetHeight = h;
        int offset = 0;
        if (aspectRatio > 0) {
            if (targetHeight * aspectRatio * 0.9 > targetWidth) {
                targetHeight = (int) (targetWidth / aspectRatio);
                offset = ((h - targetHeight) / 2) * targetWidth;
            } else if (targetHeight * aspectRatio < targetWidth * 0.9) {
                targetWidth = (int) (targetHeight * aspectRatio);
            }
        }

        Bitmap ret = Bitmap.createBitmap(pix, offset, w, targetWidth, targetHeight, bitmap.getConfig());

        return ret;
    }

    // 图片压缩或放大，同比例放大缩小
    public static Bitmap zoomBitmap(Bitmap bitmap, int ratio) {
        if (ratio == 1) {
            return bitmap;
        }
        float rate = (float) 1 / (float) Math.sqrt((double) ratio);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(rate, rate);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }


    /**
     * 依据原图的 长&宽 来裁剪图片
     * 
     * @param src
     * @param w
     *            :目标长
     * @param h
     *            :目标宽
     * @return
     */
    public static Bitmap resizePicAccordingOrg(Bitmap src, int w, int h) {

        if (src == null) {
            return null;
        }

        int imgW = src.getWidth();
        int imgH = src.getHeight();

        int sx = w >= imgW ? 0 : ((imgW - w) / 2);
        int sy = h >= imgH ? 0 : ((imgH - h) / 2);

        try {
            Bitmap b = Bitmap.createBitmap(src, sx, sy, sx == 0 ? imgW : w, sy == 0 ? imgH : h);
            return b;
        } catch (OutOfMemoryError error) {
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 图片圆角处理
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    /**
     * 图片上圆角处理，下边沿保持矩形
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundTopCornerBitmap(Bitmap bitmap, int radius) {
        Bitmap output = null;
        int d = 2 * radius;
        try {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Rect rectSource = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            final int color = 0xffeeeff1;
            final Paint paint = new Paint();
            RectF rect = new RectF(0, 0, w, h);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            Path path = new Path();
            path.moveTo(w, h);
            path.lineTo(0, h);
            path.lineTo(0, radius);
            path.arcTo(new RectF(0, 0, d, d), 180, 90);
            path.lineTo(w - radius, 0);
            path.arcTo(new RectF(w - d, 0, w, d), 270, 90);
            path.close();
            canvas.drawPath(path, paint);
            // canvas.drawRoundRect(rect, radius, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rectSource, rect, paint);
        } catch (OutOfMemoryError e) {}
        return output;
    }

    /**
     * 图片上圆角处理，下边沿保持矩形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundTopRightCornerBitmap(Bitmap bitmap, int radius) {
        Bitmap output = null;
        int d = 2 * radius;
        try {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Rect rectSource = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            final int color = 0xffeeeff1;
            final Paint paint = new Paint();
            RectF rect = new RectF(0, 0, w, h);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            Path path = new Path();
            path.moveTo(w, h);
            path.lineTo(0, h);
            path.lineTo(0, 0);
            path.lineTo(w - radius, 0);
            path.arcTo(new RectF(w - d, 0, w, d), 270, 90);
            path.close();
            canvas.drawPath(path, paint);
            // canvas.drawRoundRect(rect, radius, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rectSource, rect, paint);
        } catch (OutOfMemoryError e) {}
        return output;
    }

    /**
     * bitmap转成byte[]
     * 
     * @param bmp
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bmp) {
        return bmpToByteArray(bmp, 100);
    }

    public static byte[] bmpToByteArray(Bitmap bmp, int quality) {

        if (null == bmp) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, quality, output);
        /*
         * if (needRecycle) { bmp.recycle(); }
         */

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static final long MAX_BITMAP_SIZE_ON_ANDROID = 2048l;

    public static Bitmap viewableBitmap(Bitmap src) {
        if (src == null) {
            return src;
        }
        if (src.getWidth() <= MAX_BITMAP_SIZE_ON_ANDROID && src.getHeight() <= MAX_BITMAP_SIZE_ON_ANDROID) {
            return src;
        }
        int max = Math.max(src.getWidth(), src.getHeight());
        int w = (int) Math.min(MAX_BITMAP_SIZE_ON_ANDROID, src.getWidth() * MAX_BITMAP_SIZE_ON_ANDROID / max);
        int h = (int) Math.min(MAX_BITMAP_SIZE_ON_ANDROID, src.getHeight() * MAX_BITMAP_SIZE_ON_ANDROID / max);
        return resizeBitmap(null, src, w, h);
    }

    public static Bitmap getRoundStrokeBitmap(Bitmap src, Bitmap output, boolean hasStroke, int strokeColor) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int size = w > h ? h : w;
        if(output == null)
            output = ImgUtils.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = null;
        if (output != null)
            canvas = new Canvas(output);
        else
            return null;

        final int color = 0xff000000;
        final Paint paint = new Paint(1);
        final Rect rect = new Rect(0, 0, size, size);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Rect rectOutside = null;
        if (w > h) {
            int offset = (w - h) / 2;
            rectOutside = new Rect(offset, 0, size + offset, size);
        } else if (h > w) {
            int offset = (h - w) / 2;
            rectOutside = new Rect(0, offset, size , size+ offset);
        } else {
            rectOutside = new Rect(0, 0, size, size);
        }
        canvas.drawBitmap(src, rectOutside, rect, paint);
        if (hasStroke) {
            paint.setColor(strokeColor);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawOval(rectF, paint);
        }
        return output;
    }

    public static void saveImage(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            bitmap.compress(CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存图片
    public static void saveImageToGallery(Context context, Bitmap bmp, String fileName) {
        File file = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }


    public static Bitmap getImage(String Url) throws Exception {
        try {

            URL url = new URL(Url);

            String responseCode = url.openConnection().getHeaderField(1);
            return BitmapFactory.decodeStream(url.openStream());

        } catch (IOException e) {

            throw new Exception(e.getMessage());

        }

    }
}
