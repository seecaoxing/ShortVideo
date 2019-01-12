package com.utils.baseutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by bjcaoxing on 2018/3/19.
 */

public class DialogUtils {


    public static void showBindPhoneDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("开通需绑定常用手机号");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public static void showOpenServiceDialog(Context context, String text, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("开通", onClickListener);
        builder.create().show();
    }

    public static void showTextDialog(Context context,
                                      String title,
                                      String text,
                                      String NegativeText,
                                      String PositiveText,
                                      boolean cancelable,
                                      DialogInterface.OnClickListener positiveOnClickListener,
                                      DialogInterface.OnClickListener negativeOnClickListener
    ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setCancelable(cancelable);
        if (!TextUtils.isEmpty(NegativeText) && negativeOnClickListener != null) {
            builder.setNegativeButton(NegativeText, negativeOnClickListener);
        }

        builder.setPositiveButton(PositiveText, positiveOnClickListener);
        builder.create().show();
    }


}
