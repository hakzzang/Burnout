package com.hbs.burnout.ui.main

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun bindBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = true

    override fun transitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeMainViewModel(binding, mainViewModel)
    }

    private fun observeMainViewModel(binding: ActivityMainBinding, mainViewModel: MainViewModel){

    }

}