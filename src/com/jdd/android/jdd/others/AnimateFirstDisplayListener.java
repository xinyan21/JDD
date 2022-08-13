package com.jdd.android.jdd.others;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：首次显示图片的动画监听器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-03
 * @last_edit 2016-03-03
 * @since 1.0
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> sDisplayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !sDisplayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                sDisplayedImages.add(imageUri);
            }
        }
    }
}
