package com.example.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface IMichelangelo{
    void load(final ImageView imageView, final String uri);
    Bitmap loadSync(final String uri);
}
