package com.hbs.burnout.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.*

object FileUtils {
    const val RECOGNIZE_FILE_NAME = "recognize_result.jpg"
    const val RECOGNIZE_FILE_NAME2 = "recognize_result2.jpg"

    fun getOrMakeRecognizeFile(context: Context): File {
        return File(context.filesDir, RECOGNIZE_FILE_NAME)
    }

    fun getOrMakeRecognizeFile2(context: Context): File {
        return File(context.filesDir, RECOGNIZE_FILE_NAME2)
    }

    fun saveBitmapToFile(file: File, bitmap: Bitmap) {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
    }

    fun saveImageToExternalFilesDir(context: Context, bitmap: Bitmap): Uri {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), "BurnOut"
        )
        try {
            file.createNewFile()
            val ostream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
            ostream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val uri = FileProvider.getUriForFile(context, "com.hbs.burnout.fileprovider", file);

        return uri
    }

    fun saveImageToMediaStore(context: Context, bitmap: Bitmap, fileName: String) {
        val resolver = context.contentResolver
        val relativeLocation = Environment.DIRECTORY_DCIM + "/" + "BurnOut"

     val imageCollection = /*MediaStore.Images.Media.EXTERNAL_CONTENT_URI*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media
                .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media
                .getContentUri(MediaStore.VOLUME_EXTERNAL)
        }

        val contentValues = ContentValues().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, relativeLocation)

            }
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val imageContentUri = resolver.insert(imageCollection, contentValues)
        var stream: OutputStream? = null
        imageContentUri?.let { uri ->
            try {
                stream = resolver.openOutputStream(imageContentUri)!!
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            } catch (e: IOException) {
                resolver.delete(imageContentUri, null, null)
            } finally {
                stream?.close()
                contentValues.clear();
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
                resolver.update(uri, contentValues, null, null);
            }
        }
    }
}