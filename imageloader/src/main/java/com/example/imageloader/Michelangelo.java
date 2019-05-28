package com.example.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.imageloader.cache.memory.MemoryCache;
import com.example.imageloader.util.IMichelangeloCallback;
import com.example.imageloader.cache.disk.BitmapDiskCache;
import com.example.imageloader.cache.disk.IDiskCache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Michelangelo implements IMichelangelo {
    private static final String TAG = "Michelangelo";

    private final OkHttpClient client = new OkHttpClient();
    private final Executor executor = Executors.newCachedThreadPool();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final IDiskCache<String, Bitmap> diskCache;
    private final MemoryCache memoryCache = new MemoryCache();

    private Michelangelo(final Context context) {
        diskCache = new BitmapDiskCache(context);
    }

    private static Michelangelo singleInstance = null;

    public static Michelangelo getInstance(final Context context) {
        if (singleInstance == null)
            singleInstance = new Michelangelo(context);

        return singleInstance;
    }

    @Override
    public void load(final ImageView imageView, final String uri) {

        if (TextUtils.isEmpty(uri)) {
            return;
        }

        imageView.setTag(uri);

        loadFromMemoryCache(uri, new IMichelangeloCallback<Bitmap>() {

            @Override
            public void onResult(final Bitmap result) {
                if (result == null) {
                    imageView.setImageResource(R.drawable.preview);
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadFromDiskCache(uri, new IMichelangeloCallback<Bitmap>() {

                                @Override
                                public void onResult(final Bitmap result) {
                                    if (result == null) {
                                        try {
                                            loadFromNetwork(uri, new IMichelangeloCallback<Bitmap>() {

                                                @Override
                                                public void onResult(final Bitmap result) {
                                                    if (result == null) {
                                                        showErrorImage(uri, imageView);
                                                    } else {
                                                        showImage(uri, imageView, result); // From network
                                                    }
                                                }

                                                @Override
                                                public void onError(final Throwable throwable) {
                                                    showErrorImage(uri, imageView);
                                                }
                                            });
                                        } catch (IOException | IllegalArgumentException e) {
                                            showErrorImage(uri, imageView);
                                        }
                                    } else {
                                        showImage(uri, imageView, result); //from disk cache
                                    }
                                }

                                @Override
                                public void onError(final Throwable throwable) {
                                    showErrorImage(uri, imageView);
                                }
                            });
                        }
                    });
                } else {
                    showImage(uri, imageView, result); // from memory cache
                }
            }

            @Override
            public void onError(final Throwable throwable) {
                showErrorImage(uri, imageView);
            }
        });
    }

    @Override
    public Bitmap loadSync(final String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        final Bitmap memoryCacheBitmap = loadSyncFromMemoryCache(uri);

        if (memoryCacheBitmap != null) {
            return memoryCacheBitmap;
        } else {
            final Bitmap diskCacheBitmap = loadSyncFromDiskCache(uri);

            if (diskCacheBitmap != null) {
                putInMemoryCache(uri, diskCacheBitmap);
                return diskCacheBitmap;
            } else {
                try {
                    final Bitmap networkCacheBitmap = loadSyncFromNetwork(uri);

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            putInMemoryCache(uri, networkCacheBitmap);
                            putInDiskCache(uri, networkCacheBitmap);
                        }
                    });

                    return networkCacheBitmap;
                } catch (IOException e) {
                    return null;
                }
            }
        }

    }

    private Bitmap loadSyncFromMemoryCache(final String uri) {
        synchronized (memoryCache) {
            return memoryCache.get(uri);
        }
    }

    private void loadFromMemoryCache(final String uri, final IMichelangeloCallback<Bitmap> result) {
        synchronized (memoryCache) {
            result.onResult(memoryCache.get(uri));
        }
    }

    private Bitmap loadSyncFromDiskCache(final String uri) {
        synchronized (diskCache) {
           return diskCache.load(uri);
        }
    }
    private void loadFromDiskCache(final String uri, final IMichelangeloCallback<Bitmap> bitmapCallback) {
        synchronized (diskCache) {
            bitmapCallback.onResult(diskCache.load(uri));
        }
    }

    private Bitmap loadSyncFromNetwork(final String uri) throws IOException, IllegalArgumentException {
        final Request request = new Request.Builder().url(uri).build();
        final Response response = client.newCall(request).execute();

        return decodeImage(response);
    }
    private void loadFromNetwork(final String uri, final IMichelangeloCallback<Bitmap> callback) throws IOException, IllegalArgumentException {
        final Request request = new Request.Builder().url(uri).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                callback.onResult(null);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final Bitmap result = decodeImage(response);

                callback.onResult(result);
                putInMemoryCache(uri, result);
                putInDiskCache(uri, result);
            }
        });
    }

    private void putInMemoryCache(final String uri, final Bitmap result) {
        synchronized (memoryCache) {
            if (uri != null && result != null) {
                memoryCache.put(uri, result);
            }
        }
    }

    private void putInDiskCache(final String uri, final Bitmap result) {
        synchronized (diskCache) {
            diskCache.save(uri, result);
        }
    }

    private void showImage(final String uri, final ImageView imageView, final Bitmap bitmap) {
        if (isBitmapShouldBeSet(uri, imageView)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    imageView.setBackground(null);
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }

    private void showErrorImage(final String uri, final ImageView imageView) {
        if (isBitmapShouldBeSet(uri, imageView)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    imageView.setImageResource(R.drawable.errorview);
                }
            });
        }
    }

    private boolean isBitmapShouldBeSet(final String uri, final ImageView imageView) {
        return imageView.getTag() == null || (imageView.getTag() != null && uri.equals(imageView.getTag()));
    }

    private Bitmap decodeImage(final Response response) throws IOException {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }
}
