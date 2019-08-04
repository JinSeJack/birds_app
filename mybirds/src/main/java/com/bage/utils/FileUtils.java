package com.bage.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {

    public static boolean copy(String fileFrom, String fileTo) {

        try {
            FileInputStream in = new FileInputStream(fileFrom);
            FileOutputStream out = new FileOutputStream(fileTo);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }


    }

    /**
     * @param file                                                 ��Ҫ�����������ļ�
     * @param newLastName����׺�������ļ���(�������ļ���������Ҫ��·��)
     */
    public static File reNameTo(File file, String newLastName) {
        String path = file.getAbsolutePath();
        int i = path.lastIndexOf("\\") + 1;
        String frontPath = path.substring(0, i);

        file.renameTo(new File(frontPath + newLastName));
        System.out.println("name:" + file.getName());
        return file;

    }

    /**
     * @param urlFileName �ļ�·��+�ļ���
     * @return Bitmap���ɵ�Bitmapͼ
     */
    public static Bitmap GetBitmapByFileName(String urlFileName) {

        // ����ļ��Ƿ����
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(urlFileName, opts);
        int height = opts.outHeight;
        int width = opts.outWidth;

        // ͼƬ��ʾ��80���������ļ�
        int x = 80;
        int y = 80;

        int scaleX = width / x;
        int scaleY = height / y;

        int scale = 1;
        if (scaleX > scaleY && scaleY > 1) {
            scale = scaleX;
        }
        if (scaleY > scaleX && scaleX > 1) {
            scale = scaleY;
        }

        // ���¼���ͼƬ
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        bm = BitmapFactory.decodeFile(urlFileName, opts);
        return bm;

    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

}
