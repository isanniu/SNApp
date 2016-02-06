package com.sannniu.ncore.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对象销毁 - 工具类
 */
public class ReleaseUtils {

    /**
     * 销毁 Bitmap
     * 
     * @param bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
    }

    /**
     * 销毁 Bitmap 列表
     * 
     * @param list
     */
    public static void releaseBitmapList(ArrayList<Bitmap> list) {
        for (Bitmap bitmap : list) {
            releaseBitmap(bitmap);
        }
    }

    /**
     * 销毁 InputStream
     * 
     * @param is
     */
    public static void releaseInputstream(InputStream is) {
        if (is != null) {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁 InputStream
     * 
     * @param os
     */
    public static void releaseOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
                os = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁 HttpURLConnection
     * 
     * @param conn
     */
    public static void releaseConnection(HttpURLConnection conn) {
        if (conn != null) {
            conn.disconnect();
            conn = null;
        }
    }

    /**
     * 关闭/销毁 Cursor【游标】
     * 
     * @param cursor
     */
    public static void releaseCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    /**
     * 销毁 对象 列表
     * 
     * @param list
     */
    public static <T> void releaseList(List<T> list) {
        if (list != null) {
            list.clear();
            list = null;
        }
    }

    /**
     * 销毁 Map
     * 
     * @param map
     */
    public static <T> void releaseMap(Map<String, T> map) {
        if (map != null) {
            map.clear();
            map = null;
        }
    }

    /**
     * 销毁关闭 数据库连接
     * 
     * @param db
     */
    public static void releaseDB(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    /**
     * 销毁关闭 数据库连接 、游标
     * 
     * @param cursor
     * @param db
     */
    public static void releaseDB(Cursor cursor, SQLiteDatabase db) {
        // 先关闭游标
        releaseCursor(cursor);
        // 在关闭数据库
        releaseDB(db);
    }

    /**
     * 对象滞空
     * 
     * @param object
     */
    public static void releaseObject(Object object) {
        object = null;
    }
}
