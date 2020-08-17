package com.hbs.burnout.ui.mission

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityMissionBinding
import com.hbs.burnout.model.Chatting
import com.hbs.burnout.utils.TransitionNavigation

class MissionActivity : BaseActivity<ActivityMissionBinding>() {
    private val viewModel by viewModels<MissionViewModel>()
    private val chattingAdapter = ChattingAdapter()
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
        setHoldContainerTransition(binding.root, TransitionNavigation.MISSION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(binding)
    }

    private fun initView(binding:ActivityMissionBinding){
        binding.rvChatting.adapter = chattingAdapter
        binding.rvChatting.layoutManager = LinearLayoutManager(this)
        chattingAdapter.submitList(listOf(
            Chatting(0, "안녕하세요", 0),
            Chatting(1, "네~ 안녕하세요?", 0),
            Chatting(0, "식사는 했어요", 0),
            Chatting(0, "^ㅠ^ 난 밥먹었지롱", 0),
            Chatting(1, "ㅡㅡ나갑니다", 0)
        ))
    }
}