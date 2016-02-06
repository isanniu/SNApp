package com.sannniu.ncore.webview;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.sannniu.ncore.app.AppConfig;
import com.sannniu.ncore.app.AppConstants;
import com.sannniu.ncore.app.BaseApplication;
import com.sannniu.ncore.utils.CommonUtils;
import com.sannniu.ncore.utils.TaskExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SafeWebViewBridge.JsCallback;

/**
 * Created by niuzhikui on 2015/10/29.
 */
public class HostJsScope {

    /**
     * 短暂气泡提醒
     *
     * @param webView 浏览器
     * @param message 提示信息
     */
    public static void toast(WebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可选择时间长短的气泡提醒
     *
     * @param webView    浏览器
     * @param message    提示信息
     * @param isShowLong 提醒时间方式
     */
    public static void toast(WebView webView, String message, int isShowLong) {
        Toast.makeText(webView.getContext(), message, isShowLong).show();
    }

    /**
     * 弹出记录的测试JS层到Java层代码执行损耗时间差
     *
     * @param webView   浏览器
     * @param timeStamp js层执行时的时间戳
     */
    public static void testLossTime(WebView webView, long timeStamp) {
        timeStamp = System.currentTimeMillis() - timeStamp;
        alert(webView, String.valueOf(timeStamp));
    }

    /**
     * 系统弹出提示框
     *
     * @param webView 浏览器
     * @param message 提示信息
     */
    public static void alert(WebView webView, String message) {
        // 构建一个Builder来显示网页中的alert对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    public static void alert(WebView webView, int msg) {
        alert(webView, String.valueOf(msg));
    }

    public static void alert(WebView webView, boolean msg) {
        alert(webView, String.valueOf(msg));
    }

    /**
     * 获取设备IMSI
     *
     * @param webView 浏览器
     * @return 设备IMSI
     */
    public static String getIMSI(WebView webView) {
        return ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取用户系统版本大小
     *
     * @param webView 浏览器
     * @return 安卓SDK版本
     */
    public static int getOsSdk(WebView webView) {
        return Build.VERSION.SDK_INT;
    }

    //---------------- 界面切换类 ------------------

    /**
     * 结束当前窗口
     *
     * @param view 浏览器
     */
    public static void goBack(WebView view) {
        if (view.getContext() instanceof Activity) {
            ((Activity) view.getContext()).finish();
        }
    }

    /**
     * 传入Json对象
     *
     * @param view 浏览器
     * @param jo   传入的JSON对象
     * @return 返回对象的第一个键值对
     */
    public static String passJson2Java(WebView view, JSONObject jo) {
        Iterator iterator = jo.keys();
        String res = null;
        if (iterator.hasNext()) {
            try {
                String keyW = (String) iterator.next();
                res = keyW + ": " + jo.getString(keyW);
            } catch (JSONException je) {

            }
        }
        return res;
    }

    /**
     * 将传入Json对象直接返回
     *
     * @param view 浏览器
     * @param jo   传入的JSON对象
     * @return 返回对象的第一个键值对
     */
    public static JSONObject retBackPassJson(WebView view, JSONObject jo) {
        return jo;
    }

    public static int overloadMethod(WebView view, int val) {
        return val;
    }

    public static String overloadMethod(WebView view, String val) {
        return val;
    }

    public static class RetJavaObj {
        public int intField;
        public String strField;
        public boolean boolField;
    }

    public static List<RetJavaObj> retJavaObject(WebView view) {
        RetJavaObj obj = new RetJavaObj();
        obj.intField = 1;
        obj.strField = "mine str";
        obj.boolField = true;
        List<RetJavaObj> rets = new ArrayList<RetJavaObj>();
        rets.add(obj);
        return rets;
    }

    public static void delayJsCallBack(WebView view, int ms, final String backMsg, final JsCallback jsCallback) {
        TaskExecutor.scheduleTaskOnUiThread(ms * 1000, new Runnable() {
            @Override
            public void run() {
                try {
                    jsCallback.apply(backMsg);
                } catch (JsCallback.JsCallbackException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    public static long passLongType(WebView view, long i) {
        return i;
    }


    /**
     * 调用系统相册
     */
    public void openPhotoAlbum(WebView webView) {
        Intent albumIntent = new Intent();
        albumIntent.setType("image/*");
        albumIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (webView != null) {
            ((Activity)webView.getContext()).startActivityForResult(albumIntent, AppConfig.CODE.ACTION_ALBUM);
        } else {
            ((Activity)webView.getContext()).startActivityForResult(albumIntent, AppConfig.CODE.ACTION_ALBUM);
        }
    }

    @JavascriptInterface
    public void openNativeCamera(WebView webView) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity)webView.getContext()).startActivityForResult(intent, AppConfig.CODE.ACTION_CAMERA);
    }

    /**
     * 打开第三方应用
     *
     * @param appPackageName 应用包名
     * @param className      应用主Activity
     */
    public void openThirdApp(WebView webView, String appPackageName, String className) {
        if (webView != null) {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(appPackageName, className);
            intent.setComponent(componentName);
            webView.getContext().startActivity(intent);
        }
    }

    /**
     * 启动一个通知栏
     *
     * @param titleName   通知栏标题
     * @param contentText 通知栏内容
     */
    public void notificationBar(WebView webView, String titleName, String contentText, Intent intent, int icon) {
        Context context = BaseApplication.getInstance();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // 实例化通知栏构造器
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        //
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // 对Builder进行配置
        mBuilder.setContentTitle(titleName)//设置通知栏标题
                .setContentText(contentText)
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setTicker(titleName) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis()) //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(icon);//设置通知小ICON

        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(Notification.FLAG_ONLY_ALERT_ONCE, mBuilder.build());
    }

    /**
     * 打开扫码页面
     */
    @JavascriptInterface
    public void openDimensionCode() {
//        if (mFragment != null) {
//            Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
//            mFragment.startActivityForResult(openCameraIntent, G.ACTION_BARCODE);
//        } else {
//            Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
//            mContext.startActivityForResult(openCameraIntent, G.ACTION_BARCODE);
//        }
    }

    /**
     * 发送短信
     *
     * @param phoneNumber 要发送的号码
     * @param message     要发送的消息
     */
    @JavascriptInterface
    public void sendMessage(WebView webView, String type, String phoneNumber, String message) {
        if ("1".equals(type)) {
            String SENT_SMS_ACTION = "SENT_SMS_ACTION";
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sentPI = PendingIntent.getBroadcast(webView.getContext(), 0, sentIntent,
                    0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
        } else if ("2".equals(type)) {
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", message);
            webView.getContext().startActivity(sendIntent);
        }
    }

    /**
     * 打开录音机
     */
    public void openRecorder(WebView webView) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/amr");
        intent.setClassName("com.android.soundrecorder", "com.android.soundrecorder.SoundRecorder");
        ((Activity)webView.getContext()).startActivityForResult(intent, AppConfig.CODE.ACTION_RECORDER);
    }

    /**
     * 调用震动器
     */
    public void deviceVibrate(WebView webView) {
        CommonUtils.vibrate(webView.getContext(), 300);
    }

    /**
     * 播放音乐
     */
    public void playAudio(WebView webView) {
        CommonUtils.playMusic(webView.getContext());
    }

    /**
     * 拨打电话
     *
     * @param phoneNumber 要拨打的号码
     */
    public void openMobilePhone(WebView webView,String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        webView.getContext().startActivity(intent);
    }

    /**
     * 打开通讯录
     */
    public void openAddressList(WebView webView) {
        //打开系统联系人，查找
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
        ((Activity)webView.getContext()).startActivityForResult(intent, AppConfig.CODE.ACTION_ADDRESSLIST);
    }
}
