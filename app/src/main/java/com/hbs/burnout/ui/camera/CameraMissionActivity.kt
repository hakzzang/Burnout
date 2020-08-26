package com.hbs.burnout.ui.mission

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityCameraMissionBinding
import com.hbs.burnout.utils.ActivityNavigation
import java.io.File

private const val IMMERSIVE_FLAG_TIMEOUT = 500L

class CameraMissionActivity : BaseActivity<ActivityCameraMissionBinding>(){
    private lateinit var container: FrameLayout

    override fun bindBinding() = ActivityCameraMissionBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container = findViewById(R.id.fragment_container)
    }

    override fun onResume() {
        super.onResume()
        container.postDelayed({
            container.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    override fun isUseTransition(): Boolean = false

    override fun preTransitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun transitionLogic() {
        TODO("Not yet implemented")
    }

    companion object {
        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    override fun onBackPressed() {
        setResult(ActivityNavigation.CAMERA_TO_CHATTING)
        super.onBackPressed()
    }
}