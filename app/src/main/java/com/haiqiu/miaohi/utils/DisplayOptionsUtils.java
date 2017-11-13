package com.haiqiu.miaohi.utils;

import android.graphics.Bitmap;

import com.haiqiu.miaohi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * imageLoader参数设置
 * Created by ningl on 2016/6/4.
 */
public class DisplayOptionsUtils {
    private static DisplayImageOptions silenceDisplayBuilder;
    private static DisplayImageOptions silenceDisplayMineBgBuilder;
    private static DisplayImageOptions.Builder baseBuilder;
    private static DisplayImageOptions defaultImageOptions;
    private static DisplayImageOptions cornerImageOptions;
    private static DisplayImageOptions defaultMaxRectImageOptions;
    private static DisplayImageOptions noMemoryCache;
    private static DisplayImageOptions defaultMinRectImageOptions;

    /**
     * 获取imageloader Builder
     *
     * @return
     */
    public static DisplayImageOptions.Builder getBaseDisplayBuilder() {
        if (null == baseBuilder) {
            baseBuilder = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_default) // 设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.icon_default)// 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.icon_default) // 设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    // .delayBeforeLoading(int delayInMillis)//int
                    // delayInMillis为你设置的下载前的延迟时间
                    // 设置图片加入缓存前，对bitmap进行设置
                    // .preProcessor(BitmapProcessor preProcessor)
                    .resetViewBeforeLoading(true);// 设置图片在下载前是否重置，复位
        }
        return baseBuilder;
    }

    /**
     * 获取加载用户头像的 options
     *
     * @return
     */
    public static DisplayImageOptions getHeaderDefaultImageOptions() {
        if (null == defaultImageOptions) {
            defaultImageOptions = getBaseDisplayBuilder()
                    .showImageOnLoading(R.drawable.head_default)
                    .showImageForEmptyUri(R.drawable.head_default)
                    .showImageOnFail(R.drawable.head_default)
                    .resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return defaultImageOptions;
    }

    /**
     * 获取带默认图片的 options
     *
     * @return
     */
    public static DisplayImageOptions getNoMemoryCacheImageOptions() {
        if (null == noMemoryCache) {
            noMemoryCache = getBaseDisplayBuilder()
                    .showImageOnLoading(R.drawable.head_default)
                    .showImageForEmptyUri(R.drawable.head_default)
                    .showImageOnFail(R.drawable.head_default)
                    .resetViewBeforeLoading(false)
                    .cacheInMemory(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return noMemoryCache;
    }

    /**
     * 获取带默认图片的 options
     *
     * @return
     */
    public static DisplayImageOptions getCornerImageOptions(int corner) {
        if (null == cornerImageOptions) {
            cornerImageOptions = getBaseDisplayBuilder()
                    .showImageOnLoading(R.color.color_f1)
                    .showImageForEmptyUri(R.color.color_f1)
                    .showImageOnFail(R.color.color_f1)
                    .displayer(new RoundedBitmapDisplayer(corner))
                    .resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return cornerImageOptions;
    }

    /**
     * 获取带默认大方形图片的 options
     *
     * @return
     */
    public static DisplayImageOptions getDefaultMaxRectImageOptions() {
        if (null == defaultMaxRectImageOptions) {
            defaultMaxRectImageOptions = getBaseDisplayBuilder()
                    .showImageOnLoading(R.color.color_f1)
                    .showImageForEmptyUri(R.color.color_f1)
                    .showImageOnFail(R.color.color_f1)
                    .resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return defaultMaxRectImageOptions;
    }

    /**
     * 获取带默认小方形图片的 options
     *
     * @return
     */
    public static DisplayImageOptions getDefaultMinRectImageOptions() {
        if (null == defaultMinRectImageOptions) {
            defaultMinRectImageOptions = getBaseDisplayBuilder()
                    .showImageOnLoading(R.color.color_f1)
                    .showImageForEmptyUri(R.color.color_f1)
                    .showImageOnFail(R.color.color_f1)
                    .resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return defaultMinRectImageOptions;
    }

    /**
     * 加载图片的时候不复位
     */
    public static DisplayImageOptions getSilenceDisplayBuilder() {
        if (null == silenceDisplayBuilder) {
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(false);// 设置图片在下载前是否重置，复位
            silenceDisplayBuilder = builder.build();
        }
        return silenceDisplayBuilder;
    }

    /**
     * 获取个人主页背景 options
     *
     * @return
     */
    public static DisplayImageOptions getPersonalHomeBgOptions() {
            defaultMaxRectImageOptions = getBaseDisplayBuilder()
                    .showImageOnLoading(R.drawable.minebg)
                    .showImageForEmptyUri(R.drawable.minebg)
                    .showImageOnFail(R.drawable.minebg)
                    .resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                    .build();
        return defaultMaxRectImageOptions;
    }

    /**
     * 加载图片的时候不复位
     */
    public static DisplayImageOptions getSilenceDisplayMineBgBuilder() {
        if (null == silenceDisplayMineBgBuilder) {
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.minebg)
                    .showImageOnFail(R.drawable.minebg)
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(false);// 设置图片在下载前是否重置，复位
            silenceDisplayMineBgBuilder = builder.build();
        }
        return silenceDisplayMineBgBuilder;
    }
}
