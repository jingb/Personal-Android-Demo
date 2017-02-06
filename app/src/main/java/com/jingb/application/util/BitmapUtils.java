package com.jingb.application.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BitmapUtils {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
//            Logger.i("is bitmapDrawable");
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Logger.i("drawable.getIntrinsicWidth(): " + w + " and drawable.getIntrinsicHeight(): " + h);

        /**
         * RGB_565
         *      may be useful when using opaque bitmaps
         *      that do not require high color fidelity
         * **/
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Logger.i("drawable.getOpacity value: " + drawable.getOpacity());

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        int size = bitmap.getHeight() * bitmap.getRowBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(buffer);
        return new ByteArrayInputStream(buffer.array());
    }

    /**
     * @param sizeInKB KB为单位
     * 超过sizeInKB就压缩
     * 如果无法压缩到这个数值，则返回null
     */
    public static Bitmap compress(Bitmap image, int sizeInKB) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        Logger.i("orginal image size: " + byteArrayOutputStream.toByteArray().length / 1024 + "KB");
        if (byteArrayOutputStream.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            byteArrayOutputStream.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);// 这里压缩90%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(byteArrayInputStream, null, newOpts);
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        int height = 800;// 这里设置高度为800
        int width = 480;// 这里设置宽度为480
        // calculate scale factor
        int be = calculateInSampleSize(newOpts, width, height);
        newOpts.inSampleSize = be;
        Logger.i("inSampleSize value: " + newOpts.inSampleSize);
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream, null, newOpts);
        return compressImage(bitmap, sizeInKB);// 压缩好比例大小后再进行质量压缩
    }

    private static Bitmap compressImage(Bitmap image, int sizeInKB) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while (byteArrayOutputStream.toByteArray().length / 1024 > sizeInKB) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            byteArrayOutputStream.reset();
            image.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);// 这里压缩options%，把压缩后的数据存放到baos中
            if (quality >= 11) {
                quality -= 10;
            } else {
                return null;
            }
        }
        Logger.i("final quality: " + quality);
        Logger.i("final image size: " + byteArrayOutputStream.toByteArray().length / 1024 + "KB");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，返回值也不再是一个Bitmap对象，而是null。
     * 虽然Bitmap是null了，但是BitmapFactory.Options的outWidth、outHeight和outMimeType属性都会被赋值。
     * 这个技巧让我们可以在加载图片之前就获取到图片的长宽值和MIME类型，从而根据情况对图片进行压缩
     */
    public static Bitmap decodeBitmap(InputStream inputStream, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.decodeStream(inputStream, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

}
