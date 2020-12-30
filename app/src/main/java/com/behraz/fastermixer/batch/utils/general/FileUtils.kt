package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

object FileUtils {
    // Providing Thumbnail For Selected Image
    fun getThumbnailPathForLocalFile(context: Activity, fileUri: Uri): Bitmap {
        val fileId = getFileId(context, fileUri)
        return MediaStore.Video.Thumbnails.getThumbnail(
            context.contentResolver,
            fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null
        )
    }

    // Getting Selected File ID
    private fun getFileId(context: Activity, fileUri: Uri): Long {
        val mediaColumns = arrayOf(MediaStore.Video.Media._ID)
        val cursor = context.contentResolver.query(fileUri, mediaColumns, null, null, null)!!
        cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            cursor.close()
            return cursor.getInt(columnIndex).toLong()
        }
        cursor.close()
        return 0
    }

    fun getImagePath(context: Activity, selectedImage: Uri?): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            context.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIndex)
        cursor.close()
        return mediaPath
    }

    fun getMediaPath(context: Activity, selectedVideo: Uri?): String {
        val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
        val cursor =
            context.contentResolver.query(selectedVideo!!, filePathColumn, null, null, null)!!
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIndex)
        cursor.close()
        return mediaPath
    }
}