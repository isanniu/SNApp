package com.sannniu.ncore.utils;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by niuzhikui on 2015/11/16.
 */
public class TextUtil {
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        return null == charSequence || charSequence.toString().trim().length() <= 0;
    }

    public static boolean isLeast(Object[] objs, int count) {
        return null != objs && objs.length >= count;
    }

    public static boolean isLeast(int[] objs, int count) {
        return null != objs && objs.length >= count;
    }

    public static boolean isEquals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static String trim(CharSequence charSequence) {
        return null == charSequence ? null : charSequence.toString().trim();
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    public static String pickFirstNotNull(CharSequence... options) {
        if (isEmpty(options)) {
            return null;
        }
        String result = null;
        for (CharSequence cs : options) {
            if (null != cs) {
                result = cs.toString();
                break;
            }
        }
        return result;
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    @SafeVarargs
    public static <T> T pickFirstNotNull(Class<T> clazz, T... options) {
        if (isEmpty(options)) {
            return null;
        }
        T result = null;
        for (T obj : options) {
            if (null != obj) {
                result = obj;
                break;
            }
        }
        return result;
    }

    /**
     * 替换文本为图片
     *
     * @param charSequence
     * @param regPattern
     * @param drawable
     * @return
     */
    public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Drawable drawable) {
        SpannableString ss = charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
        try {
            ImageSpan is = new ImageSpan(drawable);
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                String key = matcher.group();
                ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ss;
    }


    /**
     * 压缩字符串到Zip
     *
     * @param str
     * @return 压缩后字符串
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压Zip字符串
     *
     * @param str
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("UTF-8"));
        return uncompress(in);
    }

    /**
     * 解压Zip字符串
     *
     * @param inputStream
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gunzip = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }

    /**
     * InputStream convert to string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    /**
     * 解压Gzip获取
     *
     * @param is
     * @return
     */
    public static String inputStream2StringFromGZIP(InputStream is) {
        StringBuilder resultSb = new StringBuilder();
        BufferedInputStream bis = null;
        InputStreamReader reader = null;
        try {
            bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip流的前两个字节是0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            while ((readSize = reader.read(data)) > 0) {
                resultSb.append(data, 0, readSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(is, bis, reader);
        }
        return resultSb.toString();
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    public static String getExceptionStackTrace(Throwable throwable) {
        if (null == throwable) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(throwable.getMessage()).append("\n");
        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement element : elements) {
            sb.append(element.toString()).append("\r\n");
        }
        return sb.toString();
    }
}
