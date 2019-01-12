package com.utils.baseutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by fanweiguo on 16/11/25.
 * 管理Glide 图片加载
 */

public class GlideImageLoader {


    public static void setImage_Normal(Context mContext, String url, ImageView imageView) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).into(imageView);
    }

    public static void setImage_Normal(Context mContext, String url, ImageView imageView, int resourceId) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).error(resourceId).into(imageView);
    }

    public static void setImage_CenterCrop(Context mContext, String url, ImageView imageView) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).centerCrop().into(imageView);
    }

    public static void setImage_CenterCrop(Context mContext, String url, ImageView imageView, int resourceId) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).centerCrop().error(resourceId).into(imageView);
    }

    public static void setImage_CenterCrop_placeholder(Context mContext, String url, ImageView imageView, int placeHolder, int errorId) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).centerCrop().placeholder(placeHolder).error(errorId).into(imageView);
    }

    public static void setImage_Normal(Activity activity, String url, ImageView imageView) {
        if (!GlideImageLoader.checkContext(activity)) return;
        Glide.with(activity).using(new GlideResizeUrlLoader(activity)).load((new GlideResizeStudio(url))).into(imageView);
    }

    public static void setImage_Normal_placeholder(Context mContext, String url, ImageView imageView, int placeHolder) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).placeholder(placeHolder).dontAnimate().into(imageView);
    }

    public static void setImage_Normal_placeholder_error(Context mContext, String url, ImageView imageView, int placeHolder, int errorId) {
        if (!GlideImageLoader.checkContext(mContext)) return;
        Glide.with(mContext).using(new GlideResizeUrlLoader(mContext)).load((new GlideResizeStudio(url))).placeholder(placeHolder).error(errorId).dontAnimate().into(imageView);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean checkContext(Context context) {
        if (context == null) return false;
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !((Activity) context).isDestroyed();
            } else {
                return !((Activity) context).isFinishing();
            }
        }
        if (context instanceof ContextWrapper) {
            return checkContext(((ContextWrapper) context).getBaseContext());
        }
        if (context instanceof FragmentActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !((FragmentActivity) context).isDestroyed();
            } else {
                return !((FragmentActivity) context).isFinishing();
            }
        }
        return true;
    }


}
