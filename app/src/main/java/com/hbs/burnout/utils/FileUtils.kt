package com.hbs.burnout.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun saveImageToExternalFilesDir(context: Context, bitmap:Bitmap): Uri {
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

        val uri =   FileProvider.getUriForFile(context, "com.hbs.burnout.fileprovider", file);

        Log.d("shareTest", "[ShareActivity] getPrivateAlbumStorage() uri = $uri")

        return uri
    }

    fun saveImageToMediaStore(context:Context, bitmap:Bitmap){

    }
}