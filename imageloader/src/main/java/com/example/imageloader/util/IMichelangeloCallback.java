package com.example.imageloader.util;

public interface IMichelangeloCallback<T> {
    void onResult(final T pResult);
    void onError(Throwable throwable);
}
