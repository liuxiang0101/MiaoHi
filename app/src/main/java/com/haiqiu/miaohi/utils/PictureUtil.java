package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 图片处理工具
 * Created by ningl on 2016/6/30.
 */
public class PictureUtil {

        /**
         * 把bitmap转换成String
         *
         * @param filePath
         * @return
         */
        public static String bitmapToString(String filePath) {

            Bitmap bm = getSmallBitmap(filePath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] b = baos.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);

        }

        /**
         * 计算图片的缩放值
         *
         * @param options
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        public static int calculateInSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            return inSampleSize;
        }




        /**
         * 根据路径获得突破并压缩返回bitmap用于显示
         *
         * @return
         */
        public static Bitmap getSmallBitmap(String filePath) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 400, 400);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(filePath, options);
        }
    /**
         * 根据路径获得突破并压缩返回bitmap用于显示
         *
         * @return
         */
        public static Bitmap getSmallBitmap(InputStream is) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(is, options);
            Rect rect = new Rect(0, 0, 400, 400);
            BitmapFactory.decodeStream(is, rect, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 480, 800);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(is, rect, options);
        }

        /**
         * 根据路径删除图片
         *
         * @param path
         */
        public static void deleteTempFile(String path) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }

        /**
         * 添加到图库
         */
        public static void galleryAddPic(Context context, String path) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(path);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }


    }