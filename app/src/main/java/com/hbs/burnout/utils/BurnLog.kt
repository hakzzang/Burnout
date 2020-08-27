package com.hbs.burnout.utils

import android.content.Context
import android.util.Log

class BurnLog {
    companion object {
        private const val TAG = "BurnOutLog"
        fun Debug(context: Any, mesage: String) {
            val tag = setTag(context)
            Log.d(TAG, "$tag:$mesage")
        }

        fun Info(context: Any, mesage: String) {
            val tag = setTag(context)
            Log.i(TAG, "$tag:$mesage")
        }

        fun Error(context: Any, mesage: String) {
            val tag = setTag(context)
            Log.e(TAG, "$tag:$mesage")
        }

        fun setTag(context: Any): String {
            return "[ ${context.javaClass.simpleName} ]"
        }
    }
}