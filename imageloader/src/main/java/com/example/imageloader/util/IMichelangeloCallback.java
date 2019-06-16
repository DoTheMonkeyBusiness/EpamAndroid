package com.example.imageloader.util;

public interface IMichelangeloCallback<T> {
    void onResult(final T result);
    void onError(Throwable throwable);
}
