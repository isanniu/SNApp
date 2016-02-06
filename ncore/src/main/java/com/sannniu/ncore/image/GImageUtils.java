package com.sannniu.ncore.image;

import android.content.Context;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ImageLoader tools
 * Created by qiudongchao on 2015/4/10.
 */
public class GImageUtils {
    /**
     * lazy load
     * ListView / public void onScrollStateChanged(AbsListView view, int scrollState) {****}
     * @param scrollState scroll state
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    public static void lazyLoad(int scrollState, boolean pauseOnScroll, boolean pauseOnFling) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                ImageLoader.getInstance().resume();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (pauseOnScroll) {
                    ImageLoader.getInstance().pause();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                if (pauseOnFling) {
                    ImageLoader.getInstance().pause();
                }
                break;
        }
    }

    /**
     * create disk cache
     * @param context
     * @return
     */
    public static DiskCache createDiskCache(Context context) {
        return DefaultConfigurationFactory.createDiskCache(context, DefaultConfigurationFactory.createFileNameGenerator(), 0, 0);
    }

    /**
     * Convert inputstream to byte[]
     * @param is
     * @return
     */
    public static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        return os.toByteArray();
    }

    /**
     * File to byte[]
     * @param f
     * @return
     */
    public static byte[] fileToBytes(File f){
        if (f == null){
            return null;
        }
        try{
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
