package com.jingb.application.ninegag.imageload.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.imageload.model.GagDatagram;
import com.jingb.application.util.BitmapUtils;
import com.jingb.application.util.ImageCacheManager;
import com.jingb.application.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jingb on 16/6/29.
 */
public class PhotoViewActivity extends BaseActivity implements View.OnLongClickListener, View.OnClickListener {

    public static final String IMAGE_URL = "largeImageUrl";
    public static final String PHOTODESC = "photoDesc";
    public static final String MEDIA = "media";
    public static final String MEDIAURL = "mediaUrl";

    /**
     * 大图超过此值就进行压缩,KB为单位
     */
    private static final int MAX_IMAGE_SIZE = 100;

    @Bind(R.id.largeImage)
    ImageView mImageView;

    /*@Bind(R.id.photoDesc)
    TextView photoDesc;*/

    PhotoViewAttacher mAttacher;

    @Bind(R.id.btn_playMedia)
    Button btnPlayMedia;

    GagDatagram.Media mMedia;

    @Bind(R.id.google_progress)
    ProgressBar mProgressBar;

    @Bind(R.id.photoViewMain)
    RelativeLayout mPhotoViewMain;

    Drawable defaultImageDrawable;
    Drawable errorImageDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);
        ButterKnife.bind(PhotoViewActivity.this);

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnLongClickListener(this);

        defaultImageDrawable = new ColorDrawable(getResources().getColor(R.color.white));
        errorImageDrawable = getResources().getDrawable(R.drawable.loading_error);
//        int num = new Random().nextInt(Jingb.COLORS.length);
//        photoDesc.setText(getIntent().getStringExtra(PHOTODESC));
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                updateView(response, isImmediate);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                mImageView.setImageDrawable(errorImageDrawable);
            }
        };
        mProgressBar.setVisibility(View.VISIBLE);
        ImageCacheManager.loadImage(getIntent().getStringExtra(IMAGE_URL), listener);

        /*Drawable drawable = getResources().getDrawable(R.drawable.test);
        mImageView.setImageBitmap(BitmapUtils.compress(BitmapUtils.drawableToBitmap(drawable), MAX_IMAGE_SIZE));*/
        //Logger.i("MeasuredHeight: " + mImageView.getMeasuredHeight() + "  MeasuredWidth:" + mImageView.getMeasuredWidth());
        //Logger.i("MeasuredHeightAndState: " + mImageView.getMeasuredHeightAndState() + "  MeasuredWidthAndState:" + mImageView.getMeasuredWidthAndState());
        //Logger.i("Height: " + mImageView.getHeight() + "  Width:" + mImageView.getWidth());
    }

    public void updateView(ImageLoader.ImageContainer response, boolean isImmediate) {
        if (response.getBitmap() != null) {
            Bitmap result = BitmapUtils.compress(response.getBitmap(), MAX_IMAGE_SIZE);
            if (result != null) {
                mImageView.setImageBitmap(result);
            } else {
                ToastUtils.showShort("the bitmap is too large and can not be loaded!");
            }
            /***
             * If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc
             * then you just need to call
             */
            mAttacher.update();
            mProgressBar.setVisibility(View.GONE);
            /**
             * display the play btn when the image is loaded fully
             */
            mMedia = (GagDatagram.Media) getIntent().getSerializableExtra(PhotoViewActivity.MEDIA);
            if (mMedia.hasMedia) {
                btnPlayMedia.setVisibility(View.VISIBLE);
                btnPlayMedia.setOnClickListener(PhotoViewActivity.this);
            }
        } else {
            mPhotoViewMain.setBackground(defaultImageDrawable);
        }
    }

    public void handleLongClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoViewActivity.this);
        builder.setItems(new String[]{"保存图片", "分享到盆友圈"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mImageView.setDrawingCacheEnabled(true);
                                Bitmap imageBitmap = mImageView.getDrawingCache();
                                if (imageBitmap != null) {
                                    //new SaveImageTask().execute(imageBitmap);
                                    HandlerThread handlerThread = new HandlerThread("jingb");
                                    handlerThread.start();
                                    /*final Handler handler = new Handler(handlerThread.getLooper()) {

                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            ToastUtils.showShort(msg.obj.toString());
                                        }

                                    };*/
                                    Handler handler = new Handler(handlerThread.getLooper());
                                    handler.post(new SavePicThread(imageBitmap, handler));
                                }
                                break;
                            case 1:
                                ToastUtils.showShort("暂未实现");
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    class SavePicThread implements Runnable {

        Bitmap mBitmap;
        Handler mHandler;

        public SavePicThread(Bitmap bitmap, Handler handler) {
            mBitmap = bitmap;
            mHandler = handler;
        }

        @Override
        public void run() {
            String sdcard = Environment.getExternalStorageDirectory().toString();

            File file = new File(sdcard + "/jingbApp");
            if (!file.exists()) {
                file.mkdirs();
            }

            File imageFile = new File(file.getAbsolutePath(), new Date().getTime() + ".jpg");
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(imageFile);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                Logger.e(e, e.getMessage());
            } catch (IOException e) {
                Logger.e(e, e.getMessage());
            }
            String resutl = "保存成功, 路径: " + imageFile.getAbsolutePath();
            //Message msg = mHandler.obtainMessage();
            /*Message msg =  Message.obtain(mHandler, new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("jingb");
                }
            });*/
            Message msg = Message.obtain();
            msg.arg1 = Jingb.SUCCESS;
            msg.obj = resutl;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        handleLongClick();
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PlayMediaActivity.class);
        intent.putExtra(MEDIAURL, mMedia.mp4);
        startActivity(intent);
    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            String result = "保存失败";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();

                File file = new File(sdcard + "/jingbApp");
                if (!file.exists()) {
                    file.mkdirs();
                }

                File imageFile = new File(file.getAbsolutePath(), new Date().getTime() + ".jpg");
                FileOutputStream outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                result = "保存成功";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ToastUtils.showLong(result);
            mImageView.setDrawingCacheEnabled(false);
        }
    }

}
