package com.hbs.burnout.ui.mission

import android.os.Build
import android.os.Bundle
import android.view.Window
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityMissionBinding
import com.hbs.burnout.utils.TransitionNavigation

class MissionActivity : BaseActivity<ActivityMissionBinding>() {
    override fun bindBinding() = ActivityMissionBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = true

    override fun preTransitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false

            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun transitionLogic() {
        setArcTransition(binding.root, TransitionNavigation.MISSION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}