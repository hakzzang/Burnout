package com.hbs.burnout.ui.chat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.core.EventObserver
import com.hbs.burnout.databinding.FragmentChattingBinding
import com.hbs.burnout.model.Script
import com.hbs.burnout.ui.drawable.DrawImageActivity
import com.hbs.burnout.ui.ext.dialog.AnswerDialog
import com.hbs.burnout.ui.ext.dialog.DrawingImageDialog
import com.hbs.burnout.ui.ext.dialog.TakePictureDialog
import com.hbs.burnout.ui.mission.CameraMissionActivity
import com.hbs.burnout.utils.ActivityNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {
    //진행하는 스테이지 인덱스 : 1부터 시작
    private val stageNumber by lazy {
        arguments?.getInt(ActivityNavigation.STAGE_ROUND, 0) ?: 0
    }

    private val viewModel by viewModels<ChattingViewModel>()
    private val chattingAdapter by lazy {
        ChattingAdapter()
    }

    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentChattingBinding =
        FragmentChattingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startStage(stageNumber) {
            initView(binding)
            observeViewModel(viewModel)
            loadChattingMessages()
        }
    }

    private fun initView(binding: FragmentChattingBinding) {
        initChattingList(binding.rvChatting)
    }

    private fun observeViewModel(viewModel: ChattingViewModel) {
        viewModel.readingScript.observe(viewLifecycleOwner, EventObserver { scripts ->
            viewModel.emitParsingScript(scripts)
        })

        viewModel.parsedScript.observe(viewLifecycleOwner, EventObserver { scriptCache ->
            updateRecyclerView(scriptCache)
        })

        viewModel.completedReadingScript.observe(viewLifecycleOwner, EventObserver { lastScript ->
            when (lastScript.event) {
                0 -> {
                    viewModel.readNextScriptLine(stageNumber)
                }
                1 -> {
                    binding.root.post { showAnswerDialog(viewModel, lastScript) }
                }
                2 -> {
                    viewModel.readNextScriptLine(stageNumber)
                }
                3 -> {
                    binding.root.post {
                        showTakePictureDialog()
                    }
                }
                4 -> {
                    viewModel.readNextScriptLine(stageNumber)
                }
                5 -> {
                    binding.root.post {
                        showDrawingImageDialog()
                    }
                }
                6 -> {
                    viewModel.readNextScriptLine(stageNumber)
                }
            }
        })

        viewModel.completedStage.observe(viewLifecycleOwner, EventObserver {
            viewModel.completeStage(stageNumber) {
                binding.root.postDelayed({
                    (activity as? ChattingActivity)?.changeNavigationGraph(stageNumber)
                }, 3000L)
            }
        })
    }

    private fun initChattingList(chattingRecyclerView: RecyclerView) {
        chattingRecyclerView.apply {
            itemAnimator = null // remove update animation
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadChattingMessages() {
        viewModel.clearScriptCache()
        viewModel.loadStage(stageNumber)
    }

    private fun updateRecyclerView(scriptCache: List<Script>) {
        chattingAdapter.submitList(scriptCache.toList())
    }

    private fun showAnswerDialog(viewModel: ChattingViewModel, lastScript: Script) {
        val dialog = AnswerDialog(lastScript.answer) { dialog, answerNumber ->
            dialog.dismiss()
            viewModel.selectAnswer(answerNumber)
        }
        dialog.showNow(parentFragmentManager, "AnswerDialog")
    }

    private fun showDrawingImageDialog() {
        val dialog = DrawingImageDialog { dialog ->
            dialog.dismiss()
            viewModel.drawingImage()
            val drawingActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                when (result.resultCode) {
                    ActivityNavigation.SHARE_TO_CHATTING -> {
                        val receiveIntent = result.data?: return@registerForActivityResult
                        val isComplete = receiveIntent.getBooleanExtra(ActivityNavigation.ANALYZE_IS_COMPLETE, false)
                        if(isComplete){
                            viewModel.drawingImage()
                        }else{
                            showDrawingImageDialog()
                        }
                    }
                    ActivityNavigation.DRAWING_TO_CHATTING-> showDrawingImageDialog()
                }
            }
            drawingActivityResult.launch(Intent(Intent(requireContext(), DrawImageActivity::class.java)))
        }
        dialog.showNow(parentFragmentManager, "DrawingImageDialog")
    }

    private fun showTakePictureDialog() {
        val dialog = TakePictureDialog { dialog ->
            dialog.dismiss()
            viewModel.takePicture()
            val cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                when (result.resultCode) {
                    ActivityNavigation.SHARE_TO_CHATTING -> {
                        val receiveIntent = result.data?: return@registerForActivityResult
                        val isComplete = receiveIntent.getBooleanExtra(ActivityNavigation.ANALYZE_IS_COMPLETE, false)
                        if(isComplete){
                            viewModel.takePicture()
                        }else{
                            showTakePictureDialog()
                        }
                    }
                    ActivityNavigation.CAMERA_TO_CHATTING-> showTakePictureDialog()

                }
            }
            cameraActivityResult.launch(Intent(Intent(requireContext(), CameraMissionActivity::class.java)))
        }
        dialog.showNow(parentFragmentManager, "TakePictureDialog")
    }
}