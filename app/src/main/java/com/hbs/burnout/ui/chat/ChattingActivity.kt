package com.hbs.burnout.ui.chat

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.ActivityChattingBinding
import com.hbs.burnout.model.Script
import com.hbs.burnout.utils.TransitionConfigure
import com.hbs.burnout.utils.TransitionNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingActivity : BaseActivity<ActivityChattingBinding>() {
    private val missionNumber = 0
    private val viewModel by viewModels<ChattingViewModel>()
    private val chattingAdapter by lazy{
        ChattingAdapter()
    }

    override fun bindBinding() = ActivityChattingBinding.inflate(layoutInflater)

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
        observeViewModel(viewModel)
        viewModel.readNextScriptLine(missionNumber)
    }

    private fun initView(binding:ActivityChattingBinding){
        initToolbar(binding.toolbar, getString(R.string.title_chatting) , true)
        initChattingList(binding.rvChatting)
        binding.tvPositive.setOnClickListener {
            viewModel.selectAnswer(0)
        }
        binding.tvNegative.setOnClickListener {
            viewModel.selectAnswer(1)
        }
    }

    private fun observeViewModel(viewModel: ChattingViewModel){
        viewModel.readingScript.observe(this, EventObserver{ script->
            viewModel.emitParsingScript(script)
        })

        viewModel.parsedScript.observe(this, Observer { scriptCache->
            updateRecyclerView(scriptCache)
        })

        viewModel.completedReadingScript.observe(this, EventObserver { lastScript->
            if(lastScript.event == 0){
                viewModel.readNextScriptLine(missionNumber)
            }
        })
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

    private fun initChattingList(chattingRecyclerView: RecyclerView){
        chattingRecyclerView.apply {
            itemAnimator = null // remove update animation
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(this@ChattingActivity)
        }
    }

    private fun updateRecyclerView(scriptCache: List<Script>){
        chattingAdapter.submitList(scriptCache.toList())
    }
}