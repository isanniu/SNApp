package com.sannniu.ncore.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

/**
 * 相机，图库调用封装
 * 
 * @author qiudongchao
 * 
 */
public class GalleryManager {
    private static final String TAG = "GalleryUtils";
    private static final int REQUEST_CODE_CAMERA = 688;
    private static final int REQUEST_CODE_SELECT_PIC_KITKAT = REQUEST_CODE_CAMERA + 1;
    private static final int REQUEST_CODE_SELECT_PIC = REQUEST_CODE_SELECT_PIC_KITKAT + 1;
    private FragmentActivity mContext;
    private String mCameraImgPath;
    private OnGalleryListener mListener;

    public GalleryManager(FragmentActivity context, OnGalleryListener listener) {
        mContext = context;
        File file = context.getExternalCacheDir();
        if(file == null){
            file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if(file == null ){
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            }
        }
        mCameraImgPath = file.getAbsolutePath() + File.separator
                + ImageUtils.getTempFileName() + ".jpg";
        Log.d(TAG,"Camera Image Path = "+mCameraImgPath);
        mListener = listener;
    }

    public interface OnGalleryListener {
        void onGallery(String imgPath);
    }

    /**
     * 图片返回结果
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void getImgResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                String pic = "";
                File f = new File(mCameraImgPath);
                if (f.exists()) {
                    pic = mCameraImgPath;
                }
                if (!TextUtils.isEmpty(pic) && mListener != null) {
                    mListener.onGallery(pic);
                }
            } else if (requestCode == REQUEST_CODE_SELECT_PIC_KITKAT) {
                String pic = getPath(data.getData());
                if (!TextUtils.isEmpty(pic) && mListener != null) {
                    mListener.onGallery(pic);
                }
            } else if (requestCode == REQUEST_CODE_SELECT_PIC) {
                String pic = selectImage(data);
                if (!TextUtils.isEmpty(pic) && mListener != null) {
                    mListener.onGallery(pic);
                }
            }
        }
    }

    /**
     * 从相机获取图片
     */
    public void getImgFromCamera() {
        final Intent intent = new Intent();
        final Intent intent_camera = mContext.getPackageManager().getLaunchIntentForPackage("com.android.camera");
        if (intent_camera != null) {
            intent.setPackage("com.android.camera");
        }
        final File f1 = new File(mCameraImgPath);
        final Uri uri = Uri.fromFile(f1);

        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mContext.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    public void getImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContext.startActivityForResult(intent, REQUEST_CODE_SELECT_PIC_KITKAT);
        } else {
            mContext.startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
        }
    }

    // //////////////////////

    /**
     * 4.4前获取图库图片地址
     * 
     * @param data
     * @return
     */
    public String selectImage(Intent data) {
        Uri selectedImage = data.getData();
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                Log.e(TAG, "It's auto backup pic path:" + selectedImage.toString());
                return null;
            }
        }
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    @SuppressLint("NewApi")
    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(mContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based
     * ContentProviders.
     * 
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
