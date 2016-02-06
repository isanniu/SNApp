package com.sannniu.ncore.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by niuzhikui on 2015/3/17.
 */
public class ToastFactory {
    private static Context context = null;
    private static Toast toast = null;

    private static Toast getToast(Context context, String text,int duration) {
        if (ToastFactory.context == context) {
            // toast.cancel();
            toast.setText(text);
            toast.setDuration(duration);

        } else {

            ToastFactory.context = context;
            toast = Toast.makeText(context, text, duration);
        }
        return toast;
    }
    public static void show(Context context, String str) {
        // Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        Toast toast=ToastFactory.getToast(context, str,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
    public static void show(Context context, int resId) {
        Toast toast=ToastFactory.getToast(context, context.getResources().getText(resId).toString(),Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, int resId, int duration) {
        Toast toast=ToastFactory.getToast(context, context.getResources().getText(resId).toString(),duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, CharSequence text) {
        Toast toast=ToastFactory.getToast(context,text.toString(),Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, CharSequence text, int duration) {
        Toast toast=ToastFactory.getToast(context,text.toString(),duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, int resId, Object... args) {
        Toast toast=ToastFactory.getToast(context, String.format(context.getResources().getString(resId), args),Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void show(Context context, String format, Object... args) {
        Toast toast=ToastFactory.getToast(context, String.format(format, args),Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void show(Context context, int resId, int duration, Object... args) {
        Toast toast=ToastFactory.getToast(context, String.format(context.getResources().getString(resId), args),duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
    public static void show(Context context, String format, int duration, Object... args) {
        Toast toast=ToastFactory.getToast(context, String.format(format, args),duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
