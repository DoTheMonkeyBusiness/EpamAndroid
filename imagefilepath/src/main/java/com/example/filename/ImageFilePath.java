package com.example.filename;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class ImageFilePath {

    private static final String NO_PATH_KEY = "Select Video Only";
    private static final String COLON_KEY = ":";
    private static final String SLASH_KEY = "/";
    private static final String DOWNLOADS_KEY = "content://downloads/public_downloads";
    private static final String PRIMARY_KEY = "primary";
    private static final String IMAGE_KEY = "image";
    private static final String VIDEO_KEY = "video";
    private static final String AUDIO_KEY = "audio";
    private static final String CONTENT_KEY = "content";
    private static final String FILE_KEY = "file";
    private static final String ID_KEY = "_id=?";
    private static final String DATA_KEY = "_data";
    private static final String EXTERNAL_STORAGE_DOCUMENTS_KEY = "com.android.externalstorage.documents";
    private static final String PROVIDERS_DOWNLOADS_DOCUMENTS_KEY = "com.android.providers.downloads.documents";
    private static final String PROVIDERS_MEDIA_DOCUMENTS_KEY = "com.android.providers.media.documents";
    private static final String APPS_PHOTOS_CONTENT_KEY = "com.google.android.apps.photos.content";

    private ImageFilePath() {

    }

    public static String getPath(final Context context, final Uri uri) {

        if (DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(COLON_KEY);
                final String type = split[0];

                if (PRIMARY_KEY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + SLASH_KEY
                            + split[1];
                }
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse(DOWNLOADS_KEY),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(COLON_KEY);
                final String type = split[0];

                Uri contentUri = null;
                if (IMAGE_KEY.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (VIDEO_KEY.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AUDIO_KEY.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, ID_KEY,
                        selectionArgs);
            }
        } else if (CONTENT_KEY.equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if (FILE_KEY.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return NO_PATH_KEY;
    }

    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {

        final String column = DATA_KEY;
        final String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection,
                selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        return NO_PATH_KEY;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return EXTERNAL_STORAGE_DOCUMENTS_KEY.equals(uri
                .getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return PROVIDERS_DOWNLOADS_DOCUMENTS_KEY.equals(uri
                .getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return PROVIDERS_MEDIA_DOCUMENTS_KEY.equals(uri
                .getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return APPS_PHOTOS_CONTENT_KEY.equals(uri
                .getAuthority());
    }
}