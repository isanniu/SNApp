package com.sannniu.ncore.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    /**
     * get ListView height according to every children
     * 
     * @param view
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }

    private static final String CLASS_NAME_GRID_VIEW = "android.widget.GridView";
    private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";

    /**
     * get GridView vertical spacing
     * 
     * @param view
     * @return
     */
    public static int getGridViewVerticalSpacing(GridView view) {
        // get mVerticalSpacing by android.widget.GridView
        Class<?> demo = null;
        int verticalSpacing = 0;
        try {
            demo = Class.forName(CLASS_NAME_GRID_VIEW);
            Field field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
            field.setAccessible(true);
            verticalSpacing = (Integer) field.get(view);
            return verticalSpacing;
        } catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException, NoSuchFieldException, InstantiationException,
             * IllegalArgumentException, IllegalAccessException, NullPointException
             */
            e.printStackTrace();
        }
        return verticalSpacing;
    }

    /**
     * get AbsListView height according to every children
     * 
     * @param view
     * @return
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }

        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }

    /**
     * set view height
     * 
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        LayoutParams params = view.getLayoutParams();
        params.height = height;
    }

    // /**
    // * set GistView height which is calculated by {@link # getGridViewHeightBasedOnChildren(GridView)}
    // *
    // * @param view
    // * @return
    // */
    // public static void setGridViewHeightBasedOnChildren(GridView view) {
    // setViewHeight(view, getGridViewHeightBasedOnChildren(view));
    // }

    /**
     * set ListView height which is calculated by {@link # getListViewHeightBasedOnChildren(ListView)}
     * 
     * @param view
     * @return
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }

    /**
     * set AbsListView height which is calculated by {@link # getAbsListViewHeightBasedOnChildren(AbsListView)}
     * 
     * @param view
     * @return
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }

    /**
     * set SearchView OnClickListener
     * 
     * @param v
     * @param listener
     */
    public static void setSearchViewOnClickListener(View v, OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView) child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }

    /**
     * get descended views from parent.
     * 
     * @param parent
     * @param filter
     *            Type of views which will be returned.
     * @param includeSubClass
     *            Whether returned list will include views which are subclass of filter or not.
     * @return
     */
    public static <T extends View> List<T> getDescendants(ViewGroup parent, Class<T> filter, boolean includeSubClass) {
        List<T> descendedViewList = new ArrayList<T>();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            Class<? extends View> childsClass = child.getClass();
            if ((includeSubClass && filter.isAssignableFrom(childsClass))
                    || (!includeSubClass && childsClass == filter)) {
                descendedViewList.add(filter.cast(child));
            }
            if (child instanceof ViewGroup) {
                descendedViewList.addAll(getDescendants((ViewGroup) child, filter, includeSubClass));
            }
        }
        return descendedViewList;
    }
    /**
     * 适用于Adapter中简化ViewHolder相关代码
     *
     * @param convertView
     * @param id
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T obtainView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View childView = holder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            holder.put(id, childView);
        }
        return (T) childView;
    }


    /**
     * view设置background drawable
     *
     * @param view
     * @param drawable
     */
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }


    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
//        int height = view.getMeasuredHeight();
//        if(0 < height){
//            return height;
//        }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
//        int width = view.getMeasuredWidth();
//        if(0 < width){
//            return width;
//        }
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
//        int width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int height = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        view.measure(width,height);

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static int getAllListViewSectionCounts(ListView lv, List dataSource) {
        if (null == lv || TextUtil.isEmpty(dataSource)) {
            return 0;
        }
        return dataSource.size() + lv.getHeaderViewsCount() + lv.getFooterViewsCount();
    }

    /**
     * 使用ColorFilter来改变亮度
     *
     * @param imageview
     * @param brightness
     */
    public static void changeBrightness(ImageView imageview, float brightness) {
        imageview.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }
    public static void changeBrightness(Drawable drawable, float brightness) {
        drawable.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }

    private static ColorMatrixColorFilter getBrightnessMatrixColorFilter(float brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(
                new float[]{
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });
        return new ColorMatrixColorFilter(matrix);
    }

    public static ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.GRAY);
    }
    public static ColorStateList createColorStateList(int normal, int pressed, int unable) {
        return createColorStateList(normal, pressed, pressed, pressed, unable);
    }
    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int checked, int unable) {
        int[] colors = new int[]{pressed, focused, checked, normal, unable};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_focused, android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_checked, android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled};
        states[4] = new int[]{};
        return new ColorStateList(states, colors);
    }

}
