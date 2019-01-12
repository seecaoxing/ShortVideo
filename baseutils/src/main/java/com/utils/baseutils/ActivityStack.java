package com.utils.baseutils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li_bin on 2016/5/17.
 */
public class ActivityStack {

    private List<Activity> mActivityList = new ArrayList<>();

    private static ActivityStack mStack;

    private ActivityStack() {
    }

    public static ActivityStack getInstance() {
        if (mStack == null) {
            synchronized (ActivityStack.class) {
                if (mStack == null) {
                    mStack = new ActivityStack();
                }
            }
        }
        return mStack;
    }

    /**
     * 打开一个Activity时添加
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 关闭一个Activity时删除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    /**
     * 退出时关闭所有的Activity
     */
    public void closeAllActivity() {
        int size = mActivityList.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = mActivityList.remove(i);
            activity.finish();
        }
    }
}
