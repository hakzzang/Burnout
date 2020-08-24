package com.hbs.burnout.ui.chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.FragmentChattingBinding
import com.hbs.burnout.model.Script
import com.hbs.burnout.ui.ext.dialog.AnswerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {
    private val stageNumber = 1
    private val viewModel by viewModels<ChattingViewModel>()
    private val chattingAdapter by lazy{
        ChattingAdapter()
    }

    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentChattingBinding = FragmentChattingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(binding)
        observeViewModel(viewModel)
        viewModel.loadStage(stageNumber)
    }

    private fun initView(binding: FragmentChattingBinding) {
        initChattingList(binding.rvChatting)
    }

    private fun observeViewModel(viewModel: ChattingViewModel){
        viewModel.readingScript.observe(viewLifecycleOwner, EventObserver{ scripts->
            viewModel.emitParsingScript(scripts)
        })

        viewModel.parsedScript.observe(viewLifecycleOwner, EventObserver { scriptCache->
            updateRecyclerView(scriptCache)
        })

        viewModel.completedReadingScript.observe(viewLifecycleOwner, EventObserver { lastScript->
            if (lastScript.event == 0) {
                viewModel.readNextScriptLine(stageNumber)
            }else if(lastScript.event == 1){
                binding.root.post { showAnswerDialog(viewModel, lastScript) }
            }
        })

        viewModel.completedStage.observe(viewLifecycleOwner, EventObserver{

        })
    }

    private fun initChattingList(chattingRecyclerView: RecyclerView){
        chattingRecyclerView.apply {
            itemAnimator = null // remove update animation
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun updateRecyclerView(scriptCache: List<Script>) {
        chattingAdapter.submitList(scriptCache.toList())
    }

    private fun showAnswerDialog(viewModel: ChattingViewModel, lastScript: Script) {
        val dialog = AnswerDialog(lastScript.answer) { dialog,answerNumber ->
            dialog.dismiss()
            viewModel.selectAnswer(answerNumber)
        }
        dialog.showNow(parentFragmentManager, "asdf")
    }
}