package com.example.imageloader.wrappers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapDiskCache implements IDiskCache<String, Bitmap> {

    private final String TAG = "BitmapDiskCache";
    private final String diskCachePath;

    public BitmapDiskCache(final Context context) {
        diskCachePath = Environment.getExternalStorageDirectory().toString();
    }

    @Override
    public boolean save(final String key, final Bitmap value) {
        try {
            final File file = new File(diskCachePath, Uri.parse(key).getLastPathSegment());
            final FileOutputStream stream = new FileOutputStream(file);

            value.compress(Bitmap.CompressFormat.JPEG, 50, stream); //TODO Handle image size compression
            stream.flush();
            stream.close();

            return true;
        } catch (final IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public Bitmap load(final String key) {
        return BitmapFactory.decodeFile(new File(diskCachePath, Uri.parse(key).getLastPathSegment()).toString());
    }
}