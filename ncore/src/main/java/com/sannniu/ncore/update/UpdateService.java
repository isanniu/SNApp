package com.sannniu.ncore.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sanniu.ncore.R;
import com.sannniu.ncore.app.AppConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 检查更新后台下载服务
 */
public class UpdateService extends Service {

    private static final int DOWNLOAD_STATE_FAILURE = -1;
    private static final int DOWNLOAD_STATE_SUCCESS = 0;
    private static final int DOWNLOAD_STATE_START = 1;
    private static final int DOWNLOAD_STATE_INSTALL = 2;
    private static final int DOWNLOAD_STATE_ERROR_SDCARD = 3;
    private static final int DOWNLOAD_STATE_ERROR_URL = 4;

    private static final int NOTIFICATION_ID = 3956;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private PendingIntent mPendingIntent = null;

    private String mDownloadSDPath;
    private String mDownloadUrl;
    private File mDestDir;
    private File mDestFile;

    private Handler.Callback mHandlerCallBack = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_STATE_SUCCESS:
                    Toast.makeText(getApplicationContext(), R.string.app_download_success, Toast.LENGTH_LONG).show();
                    install(mDestFile);
                    break;
                case DOWNLOAD_STATE_FAILURE:
                    Toast.makeText(getApplicationContext(), R.string.app_download_failure, Toast.LENGTH_LONG).show();
                    mNotificationManager.cancel(NOTIFICATION_ID);
                    break;
                case DOWNLOAD_STATE_START:
                    Toast.makeText(getApplicationContext(), R.string.app_download_start, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_INSTALL:
                    Toast.makeText(getApplicationContext(), R.string.app_download_install, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_ERROR_SDCARD:
                    Toast.makeText(getApplicationContext(), R.string.app_download_error_sdcard, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_ERROR_URL:
                    Toast.makeText(getApplicationContext(), R.string.app_download_error_url, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private Handler mHandler = new Handler(mHandlerCallBack);

    private DownloadCallBack mDownloadCallBack = new DownloadCallBack() {

        @Override
        public void onDownloading(int progress) {
            if (progress % 5 == 0 || progress == 1 || progress == 100) {
                mNotification.contentView.setProgressBar(R.id.less_app_update_progressbar, 100, progress, false);
                mNotification.contentView.setTextViewText(R.id.less_app_update_progress_text, progress + "%");
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            }
        }

        @Override
        public void onDownloaded() {
            mNotification.contentView.setViewVisibility(R.id.less_app_update_progress_block, View.GONE);
            mNotification.defaults = Notification.DEFAULT_SOUND;
            mNotification.contentIntent = mPendingIntent;
            mNotification.contentView.setTextViewText(R.id.less_app_update_progress_text, getText(R.string.app_download_notification_success));
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            if (mDestFile.exists() && mDestFile.isFile() && checkApkFile(mDestFile.getPath())) {
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_STATE_SUCCESS;
                mHandler.sendMessage(msg);
            }
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDownloadUrl = intent.getStringExtra(AppConfig.KEY_DOWNLOAD_URL);
        if (TextUtils.isEmpty(AppConfig.sDownloadSDPath)) {
            mDownloadSDPath = getPackageName() + "/download";
        } else {
            mDownloadSDPath = AppConfig.sDownloadSDPath;
        }

        if (TextUtils.isEmpty(mDownloadUrl)) {
            sendMessage(DOWNLOAD_STATE_ERROR_URL);
            return super.onStartCommand(intent, flags, startId);
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mDestDir = new File(Environment.getExternalStorageDirectory().getPath()
                    + "/" + mDownloadSDPath);
            if (mDestDir.exists()) {
                File destFile = new File(mDestDir.getPath() + "/" + URLEncoder.encode(mDownloadUrl));
                if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {

                    sendMessage(DOWNLOAD_STATE_INSTALL);
                    install(destFile);
                    stopSelf();
                    return super.onStartCommand(intent, flags, startId);
                }
            }
        } else {
            sendMessage(DOWNLOAD_STATE_ERROR_SDCARD);
            return super.onStartCommand(intent, flags, startId);
        }

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();

        mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.less_app_update_notification);

        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplicationContext(), UpdateService.class);

        mPendingIntent = PendingIntent.getActivity(UpdateService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotification.icon = AppConfig.sUpdateIcon != 0 ? AppConfig.sUpdateIcon :0;
        mNotification.tickerText = getText(R.string.app_download_notification_start);
        mNotification.contentIntent = mPendingIntent;
        mNotification.contentView.setProgressBar(R.id.less_app_update_progressbar, 100, 0, false);
        mNotification.contentView.setTextViewText(R.id.less_app_update_progress_text, "0%");
        if (AppConfig.sUpdateIcon != 0) {
            mNotification.contentView.setImageViewResource(R.id.less_app_update_progress_icon, AppConfig.sUpdateIcon);
        }
        mNotificationManager.cancel(NOTIFICATION_ID);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);

        // 启动线程开始下载
        new UpdateThread().start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 检查apk文件是否有效(是正确下载,没有损坏的)
     * @param apkFilePath
     * @return
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 调用系统Intent安装apk包
     * @param apkFile
     */
    private void install(File apkFile) {
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void sendMessage(int what) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 下载线程
     */
    class UpdateThread extends Thread {

        @Override
        public void run() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (mDestDir == null) {
                    mDestDir = new File(Environment.getExternalStorageDirectory().getPath()
                            + "/" + mDownloadSDPath);
                }

                if (mDestDir.exists() && !mDestDir.isDirectory()) {
                    mDestDir.delete();
                }

                if (mDestDir.exists() || mDestDir.mkdirs()) {
                    mDestFile = new File(mDestDir.getPath()
                            + "/" + URLEncoder.encode(mDownloadUrl));

                    if (mDestFile.exists()
                            && mDestFile.isFile()
                            && checkApkFile(mDestFile.getPath())) {
                        sendMessage(DOWNLOAD_STATE_INSTALL);
                        install(mDestFile);
                    } else {
                        try {
                            sendMessage(DOWNLOAD_STATE_START);
                            download(mDownloadUrl, mDestFile, false, new ContentValues(), mDownloadCallBack);
                        } catch (Exception e) {
                            sendMessage(DOWNLOAD_STATE_FAILURE);
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                sendMessage(DOWNLOAD_STATE_ERROR_SDCARD);
            }
            stopSelf();
        }
    }
    /**
     * 异步下载文件,支持自定义Header
     * @param downloadUrl
     * @param dest
     * @param append
     * @param header
     * @param callBack
     * @return
     * @throws Exception
     */
    public static long download(String downloadUrl, File dest, boolean append, ContentValues header, DownloadCallBack callBack) throws Exception {
        int progress = 0;
        long remoteSize = 0;
        int currentSize = 0;
        long totalSize = -1;

        if (!append && dest.exists() && dest.isFile()) {
            dest.delete();
        }

        if (append && dest.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dest);
                currentSize = fis.available();
            } catch (IOException e) {
                throw e;
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }

        InputStream is = null;
        FileOutputStream os = null;
        try {
            URL u = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            // 设置断点续传的起始位置
            if (currentSize > 0) {
                conn.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            // 自定义header
            for (Map.Entry<String, Object> entry : header.valueSet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                conn.setRequestProperty(key, value);
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                remoteSize = conn.getContentLength();
                String contentEndcoding = conn.getHeaderField("Content-Encoding");
                if (contentEndcoding != null && contentEndcoding.equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }
                os = new FileOutputStream(dest, append);
                byte buffer[] = new byte[8192];
                int readSize = 0;
                while ((readSize = is.read(buffer)) > 0) {
                    os.write(buffer, 0, readSize);
                    os.flush();
                    totalSize += readSize;
                    // 通知回调下载进度
                    if (callBack != null) {
                        progress = (int) (totalSize * 100 / remoteSize);
                        callBack.onDownloading(progress);
                    }
                }

                if (progress != 100) {
                    callBack.onDownloading(100);
                }

                if (totalSize < 0) {
                    totalSize = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }

        if (totalSize < 0) {
            throw new Exception("Download file fail: " + downloadUrl);
        }

        // 下载完成并通知回调
        if (callBack != null) {
            callBack.onDownloaded();
        }

        return totalSize;
    }

    /**
     * 下载回调:下载进度和下载完成
     * onDownloading,带一个进度值:0~100
     * onDownloaded
     */
    public interface DownloadCallBack {
        void onDownloading(int progress);
        void onDownloaded();
    }
}
