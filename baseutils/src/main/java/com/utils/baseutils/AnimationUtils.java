package com.utils.baseutils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by bjcaoxing on 2018/7/13.
 */

public class AnimationUtils {

    /**
     * 两个view从两个高度同时向上移动，并且渐变
     *
     * @param view1
     * @param view2
     */
    public static void setAnimationsTranslationYAndAlpha(View view1, View view2) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view1, "translationY", 0, -50f);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view1, "alpha", 1f, 0f);
        ObjectAnimator translationY2 = new ObjectAnimator().ofFloat(view2, "translationY", 50f, 0);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view2, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(anim, anim2, translationY, translationY2); //设置动画
        animatorSet.setDuration(500);  //设置动画时间
        animatorSet.start(); //启动
    }

    public static void setAnimationsTranslationY(View view1, float... values) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view1, "translationY", values);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(100);  //设置动画时间
        animatorSet.start(); //启动
    }


    public static void setAnimationsTranslationX(View view1, float... values) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view1, "translationX", values);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(500);  //设置动画时间
        animatorSet.start(); //启动
    }


}