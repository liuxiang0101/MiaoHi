package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

/**
 * 字体优化
 *
 * 字体资源只分配了一个实例。
 * 如果你的 APP 也在使用自定义字体，那就可以使用adb shell dumpsys meminfo <package_name|pid> 查看内存分配情况
 * Created by hackest on 16/8/22.
 */
public class TypefaceHelper {

    private static final String TAG = "TypefaceHelper";
    private static final SimpleArrayMap<String, Typeface> TYPEFACE_CACHE = new SimpleArrayMap<String, Typeface>();

    public static Typeface get(Context context, String name) {
        synchronized (TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {

                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), name);
                    TYPEFACE_CACHE.put(name, t);
                } catch (Exception e) {
                    MHLogUtil.e(TAG, "Could not get typeface '" + name
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return TYPEFACE_CACHE.get(name);
        }
    }
}
