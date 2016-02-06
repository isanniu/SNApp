package com.sannniu.ncore.image;

import android.content.Context;

import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;

/**
 * Created by qiudongchao on 2015/4/24.
 */
public class GImageDownloader extends BaseImageDownloader {
    public GImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);

        InputStream imageStream;
        try {
            int redirectCount = 0;
            while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
                conn = createConnection(conn.getHeaderField("Location"), extra);
                redirectCount++;
            }

            if (!shouldBeProcessed(conn)) {
                return null;
            }

            imageStream = conn.getInputStream();
        } catch (SocketException se){
            if(conn != null){
                readAndCloseStream(conn.getErrorStream());
            }
            return null;
        } catch (IOException e) {
            if(conn != null){
                readAndCloseStream(conn.getErrorStream());
            }
            return null;
        }

        return new ContentLengthInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
    }

    private void readAndCloseStream(InputStream instream){
        if(instream != null){
            IoUtils.readAndCloseStream(instream);
        }
    }
}
