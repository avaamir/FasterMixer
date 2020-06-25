package com.behraz.fastermixer.batch.utils.general;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {


    // Providing Thumbnail For Selected Image
    public static Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri) {
        long fileId = getFileId(context, fileUri);
        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }

    // Getting Selected File ID
    private static long getFileId(Activity context, Uri fileUri) {
        String[] mediaColumns = {MediaStore.Video.Media._ID};
        Cursor cursor = context.getContentResolver().query(fileUri, mediaColumns, null, null, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            cursor.close();
            return cursor.getInt(columnIndex);
        }
        cursor.close();
        return 0;
    }

    public static String getImagePath(Activity context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String mediaPath = cursor.getString(columnIndex);
        cursor.close();

        return mediaPath;
    }

    public static String getMediaPath(Activity context, Uri selectedVideo) {

        String[] filePathColumn = {MediaStore.Video.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String mediaPath = cursor.getString(columnIndex);
        cursor.close();

        return mediaPath;

    }
}
