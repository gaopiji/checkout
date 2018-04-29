package com.microsoft.payment.Common.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by v-pigao on 2/27/2018.
 */

public class fileUtils {
    //public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";

    /**
     * 向本地SD卡写网络图片
     *
     * @param bitmap
     */
    public static void saveBitmapToLocal(Context context ,String fileName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName

            File imagePath = new File(context.getFilesDir(), "images");
            File newFile = new File(imagePath, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = newFile.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(newFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(Context context, String fileName) {
        try {
            File imagePath = new File(context.getFilesDir(), "images");
            File newFile = new File(imagePath, fileName);
            if (newFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        newFile));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
