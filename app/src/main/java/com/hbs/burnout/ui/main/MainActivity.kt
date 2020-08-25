package com.hbs.burnout.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.ActivityMainBinding
import com.hbs.burnout.model.Stage
import com.hbs.burnout.ui.chat.ChattingActivity
import com.hbs.burnout.ui.ext.view.hideBottomDrawer
import com.hbs.burnout.ui.main.adapter.BadgeAdapter
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
    private val badgeAdapter = BadgeAdapter {}

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
         setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        observeMainViewModel(mainViewModel)
        initView(binding)
    }

    private fun observeMainViewModel(mainViewModel: MainViewModel) {
        mainViewModel.startChatting.observe(this, EventObserver {
            startChattingActivity(it)
        })

        mainViewModel.stages.observe(this, Observer { stages ->
            initBottomDrawer(stages)
            missionAdapter.submitList(stages)
            badgeAdapter.submitList(stages)
        })
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.rvMission.adapter = missionAdapter
        binding.rvMission.layoutManager = LinearLayoutManager(binding.root.context)
        binding.bar.setNavigationOnClickListener { toggleBottomDrawer() }
        binding.bottomDrawer.hideBottomDrawer()
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

    private fun toggleBottomDrawer() {
        val bottomDrawerBehavior = BottomSheetBehavior.from(binding.bottomDrawer)
        if (bottomDrawerBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else if (bottomDrawerBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initBottomDrawer(stageList: List<Stage>) {
        var completedStage = 0
        for (stage in stageList) {
            if (stage.isCompleted()) {
                completedStage++
            }
        }
        val spannableString = SpannableString("당신은 ${completedStage}번째 미션으로\n아래의 뱃지를 획득했습니다")
        if (stageList.size < 10) {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                4,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                4, 6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvBadgeTitle.text = resources.getString(R.string.title_badge)
        binding.tvBadgeContent.text = spannableString
        binding.rvBadge.adapter = badgeAdapter
    }

    override fun onBackPressed() {
        val bottomDrawerBehavior = BottomSheetBehavior.from(binding.bottomDrawer)
        if (bottomDrawerBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }

    }
}