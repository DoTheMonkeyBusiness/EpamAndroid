package com.example.imageloader.request;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public interface IImageRequest {
    String getUrl();
    WeakReference<ImageView> getTarget();
    Bitmap getErrorImage();

    boolean isSaved();
    boolean isScaled();
}
