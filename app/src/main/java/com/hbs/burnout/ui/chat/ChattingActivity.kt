package com.hbs.burnout.ui.chat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityChattingBinding
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.NotificationHelper
import com.hbs.burnout.utils.TransitionConfigure
import com.hbs.burnout.utils.TransitionNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChattingActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun bindBinding() = ActivityChattingBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = true

    override fun preTransitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false
            window.statusBarColor =  resources.getColor(R.color.color_dialog_bg)
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun transitionLogic() {
        val transitionType = intent.getStringExtra(TransitionConfigure.TRANSITION_TYPE)
        if (transitionType == TransitionConfigure.ARC_TYPE) {
            setArcTransition(binding.root, TransitionNavigation.CHATTING_TRANSITION_ARC)
        } else {
            setHoldContainerTransition(binding.root, TransitionNavigation.CHATTING_TRANSITION_LINEAR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPostponedEnterTransition()

        bindNavigationGraph(R.navigation.nav_chatting_graph)
        initView(binding)
        val stageRound = intent.getIntExtra(ActivityNavigation.STAGE_ROUND, 0)

        updateShortcuts(this, stageRound)
        showBubble()
    }

    private fun initView(binding: ActivityChattingBinding) {
        initToolbar(binding.toolbar, getString(R.string.title_chatting), true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindNavigationGraph(@NavigationRes graphId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController
        val stageNumber = intent.getIntExtra(ActivityNavigation.STAGE_ROUND, 0)
        navController.setGraph(graphId, Bundle().apply {
            putInt(ActivityNavigation.STAGE_ROUND, stageNumber)
        })

    }

    fun changeNavigationGraph(stageNumber: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController

        navController.popBackStack(R.id.chatting_fragment, true);
        navController.navigate(R.id.completed_stage_fragment,
            Bundle().apply {
                putInt("stageNumber", stageNumber)
            })
    }

    private fun updateShortcuts(context: Context, stageNumber: Int) {
        notificationHelper.updateShortcuts(context, stageNumber)
    }

    private fun showBubble() {
        notificationHelper.makeNotificationChannel(this)
        notificationHelper.showBubble(this)
    }
}