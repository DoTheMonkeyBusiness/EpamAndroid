package com.example.imageloader.cache.memory;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class MemoryCache extends android.support.v4.util.LruCache<String, Bitmap> implements IMemoryCache {

    private static final int BYTE_KEY = 1024;
    private static final int PART_OF_MEMORY_KEY = 5;

    public MemoryCache() {
        super((int) (Runtime.getRuntime().maxMemory() / BYTE_KEY / PART_OF_MEMORY_KEY));
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return value.getByteCount() / BYTE_KEY;
    }
}
