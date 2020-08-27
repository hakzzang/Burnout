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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.ActivityMainBinding
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.StageProgress
import com.hbs.burnout.ui.chat.ChattingActivity
import com.hbs.burnout.ui.ext.view.hideBottomDrawer
import com.hbs.burnout.ui.main.adapter.BadgeAdapter
import com.hbs.burnout.ui.main.adapter.MissionAdapter
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
    private val missionAdapter = MissionAdapter(
        { itemView, position -> startActivityWithLinearTransition(itemView, position) },
        { isCompleted -> showMissionHintDialog(isCompleted) }
    )
    private val badgeAdapter = BadgeAdapter { isCompleted -> showMissionHintDialog(isCompleted) }

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
        postponeEnterTransition()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        startPostponedEnterTransition()
        observeMainViewModel(mainViewModel)
        initView(binding)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.selectStages()

    }

    private fun observeMainViewModel(mainViewModel: MainViewModel) {
        mainViewModel.startChatting.observe(this, EventObserver {
            startChattingActivityWithArcTransition(it)
        })

        mainViewModel.stages.observe(this@MainActivity, Observer { stages ->
            initBottomDrawer(stages)
            lifecycleScope.launchWhenResumed {
                missionAdapter.submitList(stages.toList())
                missionAdapter.notifyItemRangeChanged(0, missionAdapter.itemCount)
                badgeAdapter.submitList(stages.toList())
            }

//            missionAdapter.notifyDataSetChanged()
        })
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.rvMission.adapter = missionAdapter
        binding.rvMission.layoutManager = LinearLayoutManager(binding.root.context)
        binding.bar.setNavigationOnClickListener { toggleBottomDrawer() }
        binding.bottomDrawer.hideBottomDrawer()
    }

    private fun startChattingActivityWithArcTransition(view: View) {
        val intent = Intent(view.context, ChattingActivity::class.java)
        intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.ARC_TYPE)
        var stageRound = 0
        val stages = mainViewModel.stages.value ?: return

        for (stage in stages) {
            if (stage.progress == StageProgress.PLAYING || stage.progress == StageProgress.NOT_COMPLETED) {
                break
            }
            stageRound++
        }
        intent.putExtra(ActivityNavigation.STAGE_ROUND, stageRound + 1)
        startActivityResultWithTransition(
            view,
            intent,
            ActivityNavigation.CHATTING,
            TransitionNavigation.CHATTING
        )
    }

    private fun startActivityWithLinearTransition(itemView: View, position: Int) {
        val intent = Intent(itemView.context, ChattingActivity::class.java)
        intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.LINEAR_TYPE)
        intent.putExtra(ActivityNavigation.STAGE_ROUND, position + 1)
        startActivityResultWithTransition(
            itemView,
            intent,
            ActivityNavigation.CHATTING,
            TransitionNavigation.CHATTING
        )
    }

    private fun showMissionHintDialog(isCompleted: Boolean) {
        if (!isCompleted) {
            MaterialAlertDialogBuilder(this, R.style.OutlinedCutDialog)
                .setTitle("미션진행 불가")
                .setMessage("아직 수행하지 않은 미션이 있습니다.\n해당 미션을 진행하고 다시 시도해주세요.")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
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