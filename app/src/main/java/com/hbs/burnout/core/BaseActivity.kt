package com.hbs.burnout.core

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    lateinit var binding: B
    abstract fun isUseTransition(): Boolean
    abstract fun transitionLogic()
    abstract fun bindBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isUseTransition()) {
            transitionLogic()
        }
        super.onCreate(savedInstanceState)
        binding = bindBinding()

        rotationDevice(checkHorizontal())
        toggleDarkTheme(checkDarkTheme())
        setContentView(binding.root)
        setLifeCycleOwner(binding, this)
    }

    private fun setLifeCycleOwner(binding: B, lifecycleOwner: LifecycleOwner) {
        binding.lifecycleOwner = lifecycleOwner
    }

    private fun toggleDarkTheme(isUsingDarkMode: Boolean) {
        if (isUsingDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    @Suppress("DEPRECATION")
    private fun rotationDevice(isHorizontal: Boolean) {
        if (isHorizontal) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }

    private fun checkHorizontal() = false

    private fun checkDarkTheme() = false
}