package com.hbs.burnout.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.ActivityMainBinding
import com.hbs.burnout.ui.chat.ChattingActivity
import com.hbs.burnout.ui.main.adapter.MissionAdapter
import com.hbs.burnout.ui.mission.CameraMissionActivity
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.NotificationHelper
import com.hbs.burnout.utils.TransitionConfigure
import com.hbs.burnout.utils.TransitionNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var notificationHelper: NotificationHelper
    private val mainViewModel by viewModels<MainViewModel>()
    private val missionAdapter = MissionAdapter { itemView -> clickMissionList(itemView) }

    override fun bindBinding(): ActivityMainBinding {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = mainViewModel
        return binding
    }

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
        observeMainViewModel(mainViewModel)
        initView(binding)

    }

    private fun observeMainViewModel(mainViewModel: MainViewModel) {
        mainViewModel.startChatting.observe(this, EventObserver {
            startChattingActivity(it)
        })

        mainViewModel.stages.observe(this, EventObserver { stages ->
            missionAdapter.submitList(stages)
        })
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.rvMission.adapter = missionAdapter
        binding.rvMission.layoutManager = LinearLayoutManager(binding.root.context)
    }

    private fun startChattingActivity(view: View) {
        val intent = Intent(view.context, ChattingActivity::class.java)
        intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.ARC_TYPE)
        startActivityResultWithTransition(
            view,
            intent,
            ActivityNavigation.CHATTING,
            TransitionNavigation.CHATTING
        )
    }

    private fun clickMissionList(itemView: View) {
        //            val intent = Intent(itemView.context, MissionActivity::class.java)
        val intent = Intent(itemView.context, CameraMissionActivity::class.java)
        intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.LINEAR_TYPE)
        startActivityResultWithTransition(
            itemView,
            intent,
            ActivityNavigation.CHATTING,
            TransitionNavigation.CHATTING
        )
    }
}