package com.jingb.application.ninegag.fresco;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jingb.application.R;

public class FrescoMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Fresco初始化 **/
        Fresco.initialize(this);
        setContentView(R.layout.fresco_main);

        String imageUrl = "http://s.cn.bing.net/az/hprichbg/rb/UgabRiver_ZH-CN9917952183_1920x1080.jpg";
        String wrongImageUrl = imageUrl + "dd";
        Uri uri = Uri.parse(imageUrl);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .setPlaceholderImage(getResources().getDrawable(R.drawable.loading))
                .setFailureImage(getResources().getDrawable(R.drawable.loading_error))
                .setRoundingParams(RoundingParams.asCircle())
                .build();
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.simpleDraweeView);
        //draweeView.setImageURI(uri);
        draweeView.setHierarchy(hierarchy);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(getControllerListener())
                .setUri(uri)
                .build();
        draweeView.setController(controller);


        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setPostprocessor(new BasePostprocessor() {
                                    @Override
                                    public void process(Bitmap bitmap) {
                                        super.process(bitmap);
                                    }
                                })
                .build();

    }

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
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                Toast.makeText(FrescoMainActivity.this, "onFinalImageSet", Toast.LENGTH_SHORT).show();
            }

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
