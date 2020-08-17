package com.hbs.burnout.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityMainBinding
import com.hbs.burnout.ui.mission.MissionActivity
import com.hbs.burnout.ui.share.ShareActivity
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.TransitionNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun bindBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = true

    override fun preTransitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun transitionLogic() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeMainViewModel(binding, mainViewModel)
        initView(binding)
    }

    private fun observeMainViewModel(binding: ActivityMainBinding, mainViewModel: MainViewModel) {

    }

    private fun initView(binding: ActivityMainBinding) {
        val missionAdapter = MissionAdapter { itemView ->
//            val intent = Intent(itemView.context, MissionActivity::class.java)
            val intent = Intent(itemView.context, ShareActivity::class.java)
            startActivityResultWithTransition(
                itemView,
                intent,
                ActivityNavigation.MISSION,
                TransitionNavigation.MISSION
            )
        }
        binding.rvMission.adapter = missionAdapter
        binding.rvMission.layoutManager = LinearLayoutManager(binding.root.context)
        missionAdapter.submitList(mutableListOf("a", "b", "c", "d", "e", "f", "g", "h"))
    }
}