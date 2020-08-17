package com.hbs.burnout.core

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.Hold
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.utils.BurnoutTransitionManagerImpl

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    private val transitionManager = BurnoutTransitionManagerImpl()
    lateinit var binding: B
    abstract fun isUseTransition(): Boolean
    abstract fun preTransitionLogic()
    abstract fun transitionLogic()
    abstract fun bindBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isUseTransition()) {
            preTransitionLogic()
        }
        super.onCreate(savedInstanceState)
        binding = bindBinding()
        if(isUseTransition()){
            transitionLogic()
        }

//        rotationDevice(checkHorizontal())
//        toggleDarkTheme(checkDarkTheme())
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

    fun startActivityResultWithTransition(view:View, intent: Intent, requestCode: Int, transitionName: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                transitionName  // The transition name to be matched in Activity B.
            )
            val startActivityForResult = ActivityResultContracts.StartActivityForResult()
            startActivityForResult.parseResult(requestCode, intent)
            registerForActivityResult(startActivityForResult){

            }.launch(intent, options)
        } else {
            val startActivityForResult = ActivityResultContracts.StartActivityForResult()
            startActivityForResult.parseResult(requestCode, intent)
            registerForActivityResult(startActivityForResult){

            }.launch(intent)
        }
    }

    fun setArcTransition(rootView: View, transitionName:String) {
        val enterTransition = transitionManager.setStartArcFadeInTransition()
        val returnTransition = transitionManager.setReturnArcFadeOutTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.transitionName = transitionName

            enterTransition?.addTarget(rootView)
            enterTransition?.setAllContainerColors(
                MaterialColors.getColor(
                    rootView, R.attr.colorSurface
                ))
            returnTransition?.addTarget(rootView)
            returnTransition?.setAllContainerColors(
                MaterialColors.getColor(
                    rootView, R.attr.colorSurface
                ))

            window.sharedElementsUseOverlay = false
            window.sharedElementEnterTransition = enterTransition
            window.sharedElementReturnTransition = returnTransition
        }
    }

    fun setHoldContainerTransition(rootView: View, transitionName:String) : com.google.android.material.transition.platform.MaterialContainerTransform?{
        val enterTransition = transitionManager.setLinearTransition(false)
        val returnTransition = transitionManager.setLinearTransition(true)
        val hold = Hold()
        hold.addTarget(rootView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.transitionName = transitionName
            enterTransition?.addTarget(rootView)
            enterTransition?.setAllContainerColors(
                MaterialColors.getColor(
                    rootView, R.attr.colorSurface
                ))
            returnTransition?.addTarget(rootView)

            setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
            window.sharedElementsUseOverlay = false
            window.sharedElementEnterTransition = enterTransition
            window.sharedElementReturnTransition = returnTransition
        }

        return enterTransition
    }

    private fun checkHorizontal() = false

    private fun checkDarkTheme() = false
}