package com.hbs.burnout.ui.mission

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityMissionBinding
import com.hbs.burnout.utils.TransitionConfigure
import com.hbs.burnout.utils.TransitionNavigation

class MissionActivity : BaseActivity<ActivityMissionBinding>() {
    private val missionNumber = 0
    private val viewModel by viewModels<MissionViewModel>()
    private val chattingAdapter by lazy{
        ChattingAdapter()
    }

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
        val transitionType = intent.getStringExtra(TransitionConfigure.TRANSITION_TYPE)
        if(transitionType==TransitionConfigure.ARC_TYPE){
            setArcTransition(binding.root, TransitionNavigation.MISSION)
        }else{
            setHoldContainerTransition(binding.root, TransitionNavigation.MISSION)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(binding)
        observeViewModel()
    }

    private fun initView(binding:ActivityMissionBinding){
        binding.rvChatting.apply {
            itemAnimator = null // remove update animation
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(this@MissionActivity)
        }

    }

    private fun observeViewModel(){
        viewModel.makeScriptLiveData(missionNumber).observe(this, Observer{
            chattingAdapter.submitList(it.toList())
        })
    }
}