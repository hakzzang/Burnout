package com.hbs.burnout.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.*

object FileUtils {
    const val RECOGNIZE_FILE_NAME = "recognize_result.jpg"
    fun getOrMakeRecognizeFile(context: Context): File {
        return File(context.filesDir, RECOGNIZE_FILE_NAME)
    }

    fun saveBitmapToFile(file:File, bitmap: Bitmap){
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

        Log.d("shareTest", "[ShareActivity] getPrivateAlbumStorage() uri = $uri")

        return uri
    }

    fun saveImageToMediaStore(context: Context, bitmap: Bitmap, fileName: String) {
        val resolver = context.contentResolver

        val imageCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL)
            }

        val imageDetails = ContentValues().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        }

        val imageContentUri = resolver.insert(imageCollection, imageDetails)

        imageContentUri?.let { uri ->
            resolver.openFileDescriptor(uri, "w", null).use { pdf ->
                val inputStream = getImageInputStream(bitmap)
                val strToByte = getBytes(inputStream!!)
                val fos = FileOutputStream(pdf?.getFileDescriptor())
                fos.write(strToByte)
                fos.close()
                inputStream.close()
                pdf?.close()
                resolver.update(uri, imageDetails, null, null)
            }

            imageDetails.clear();
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(uri, imageDetails, null, null);
        }
    }


    private fun getImageInputStream(bitmap: Bitmap): InputStream? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData: ByteArray = bytes.toByteArray()
        return ByteArrayInputStream(bitmapData)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also({ len = it }) != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}