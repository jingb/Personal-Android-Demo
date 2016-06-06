package com.jingb.application.ninegag.fresco;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.jingb.application.Jingb;
import com.jingb.application.R;

import butterknife.ButterKnife;

public class FrescoMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Fresco初始化 **/
        Fresco.initialize(this);
        setContentView(R.layout.fresco_main);
        ButterKnife.bind(FrescoMainActivity.this);

        String imageUrl = "http://s.cn.bing.net/az/hprichbg/rb/UgabRiver_ZH-CN9917952183_1920x1080.jpg";
        String wrongImageUrl = imageUrl + "dd";
        Uri uri = Uri.parse(imageUrl);

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        Log.i(Jingb.TAG, "是否在内存中 " + imagePipeline.isInBitmapMemoryCache(uri));

        Drawable defaultImageDrawable = new ColorDrawable(
                getResources().getColor(android.R.color.holo_blue_bright));

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                //.setPlaceholderImage(getResources().getDrawable(R.drawable.loading))
                .setPlaceholderImage(defaultImageDrawable)
                .setFailureImage(getResources().getDrawable(R.drawable.loading_error))
                .build();
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.simpleDraweeView);
        //draweeView.setImageURI(uri);
        draweeView.setHierarchy(hierarchy);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(getControllerListener())
                .setUri(uri)
                .build();
        draweeView.setController(controller);

        /*ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setPostprocessor(new BasePostprocessor() {
                                    @Override
                                    public void process(Bitmap bitmap) {
                                        super.process(bitmap);
                                    }
                                })
                .build();*/

    }

    /**
     * 也许想在图片下载完成后执行一些动作，比如使某个别的 View 可见，或者显示一些文字。
     * 也许还想在下载失败后做一些事，比如向用户显示一条失败信息
     */
    public ControllerListener getControllerListener() {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                Toast.makeText(FrescoMainActivity.this,
                "onFinalImageSet事件在图片下载完成后执行一些动作", Toast.LENGTH_LONG).show();
            }

            //渐进式图片相关
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                //FLog.d("Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
                Toast.makeText(FrescoMainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        };
        return controllerListener;
    }


}
