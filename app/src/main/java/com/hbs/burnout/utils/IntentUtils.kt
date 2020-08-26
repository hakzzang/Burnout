package com.hbs.burnout.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hbs.burnout.R


object IntentUtils {

    fun createFacebookIntent():Intent{
        val intent = Intent()

        return intent
    }

    fun createInstagramFeedIntent(context:Context?, uri: Uri?):Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            setPackage("com.instagram.android")
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        return Intent.createChooser(intent, context?.resources?.getString(R.string.share_to_instagram))
    }

    fun createInstagramStoryIntent(backgroundAssetUri: Uri?):Intent{
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.apply {
            setPackage("com.instagram.android")
            setDataAndType(backgroundAssetUri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        return intent
    }
}