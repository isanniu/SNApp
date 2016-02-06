package com.sannniu.ncore.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sanniu.ncore.BuildConfig;

import java.io.File;

/**
 * @Description UniversalImageLoader v1.9.3
 */
public class GImage {

    /**
     * @param context
     * File cacheDir = StorageUtils.getCacheDirectory(context);
     * ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
     * .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
     * .diskCacheExtraOptions(480, 800, null)
     * .taskExecutor(...)
     * .taskExecutorForCachedImages(...)
     * .threadPoolSize(3) // default
     * .threadPriority(Thread.NORM_PRIORITY - 2) // default
     * .tasksProcessingOrder(QueueProcessingType.FIFO) // default
     * .denyCacheImageMultipleSizesInMemory()
     * .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
     * .memoryCacheSize(2 * 1024 * 1024)
     * .memoryCacheSizePercentage(13) // default
     * .diskCache(new UnlimitedDiscCache(cacheDir)) // default
     * .diskCacheSize(50 * 1024 * 1024)
     * .diskCacheFileCount(100)
     * .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
     * .imageDownloader(new BaseImageDownloader(context)) // default
     * .imageDecoder(new BaseImageDecoder()) // default
     * .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
     * .writeDebugLogs()
     * .build();
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(
                context, "imageloader/Cache");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 加载图片时会在内存中加载缓存
                .cacheOnDisk(true) // 加载图片时会在磁盘中加载缓存
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3)
                        // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                        // default
                .diskCache(new UnlimitedDiskCache(cacheDir))
                        // default
                .diskCacheSize(20 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
                .defaultDisplayImageOptions(defaultOptions) // default
                .imageDecoder(new GImageDecoder(BuildConfig.DEBUG))
                .imageDownloader(new GImageDownloader(context))
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * ***********************************clear cache***********************************
     */

    /**
     * clear all cache
     */
    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * clear all disk cache
     */
    public static void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    /**
     * ***********************************image show***********************************
     */

    /**
     * @param uri
     * @param imageAware
     * @date 2014-12-29 7:00:25
     */
    public static void displayImage(String uri, ImageAware imageAware) {
        ImageLoader.getInstance().displayImage(uri, imageAware);
    }

    public static void displayImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    /**
     * @param uri
     * @param imageAware
     * @param listener
     * @date 2014-12-29 7:01:32
     */
    public static void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageAware, listener);
    }

    public static void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, listener);
    }

    /**
     * @param uri
     * @param imageAware
     * @param options
     * @date 2014-12-29 7:03:13
     */
    public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(uri, imageAware, options);
    }

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    /**
     * @param uri
     * @param imageAware
     * @param options
     * @param listener
     * @date 2014-12-29 7:04:52
     */
    public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
                                    ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageAware, options, listener);
    }

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                                    ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    /**
     * @param uri
     * @param imageAware
     * @param options
     * @param listener
     * @param progressListener
     * @date 2014-12-29 7:07:02
     */
    public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
                                    ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageAware, options, listener, progressListener);
    }

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                                    ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
    }

    /**
     * ***********************************image load***********************************
     */

    /**
     * @param uri
     * @param listener
     * @date 2014-12-29 7:13:24
     */
    public static void loadImage(String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, listener);
    }

    /**
     * @param uri
     * @param targetImageSize
     * @param listener
     * @date 2014-12-29 7:14:19
     */
    public static void loadImage(String uri, ImageSize targetImageSize, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, targetImageSize, listener);
    }

    /**
     * @param uri
     * @param options
     * @param listener
     * @date 2014-12-29 7:15:14
     */
    public static void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, options, listener);
    }

    /**
     * @param uri
     * @param options
     * @param listener
     * @date 2014-12-29 7:15:14
     */
    public static void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
                                 ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, targetImageSize, options, listener);
    }

    /**
     * @param uri
     * @param options
     * @param listener
     * @date 2014-12-29 7:15:14
     */
    public static void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
                                 ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().loadImage(uri, targetImageSize, options, listener, progressListener);
    }

    /**
     * **********************************************************************
     */
    /**
     * @param uri
     * @return
     * @date 2014-12-29 7:18:12
     */
    public static Bitmap loadImageSync(String uri) {
        return ImageLoader.getInstance().loadImageSync(uri);
    }

    /**
     * @param uri
     * @param options
     * @return
     * @date 2014-12-29 7:19:44
     */
    public static Bitmap loadImageSync(String uri, DisplayImageOptions options) {
        return ImageLoader.getInstance().loadImageSync(uri, options);
    }

    /**
     * @param uri
     * @param targetImageSize
     * @date 2014-12-29 7:20:04
     */
    public static Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
        return ImageLoader.getInstance().loadImageSync(uri, targetImageSize);
    }

    /**
     * @param uri
     * @param targetImageSize
     * @param options
     * @date 2014-12-29 7:20:28
     */
    public static Bitmap loadImageSync(String uri, ImageSize targetImageSize, DisplayImageOptions options) {
        return ImageLoader.getInstance().loadImageSync(uri, targetImageSize, options);
    }

}
