package com.hbs.burnout.ui.share

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityShareBinding
import com.hbs.burnout.model.ShareResult
import com.hbs.burnout.ui.save.SaveDialog
import com.hbs.burnout.utils.FileUtils

class ShareActivity : BaseActivity<ActivityShareBinding>() {

    private lateinit var uri: Uri

    private val viewModel by viewModels<ShareViewModel>()
    private val progressAdapter = ProgressAdapter()

    override fun bindBinding() = ActivityShareBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = false

    override fun preTransitionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.sharedElementsUseOverlay = false
            setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        }
    }

    override fun transitionLogic() {
        TODO("Not yet implemented")
    }


    private fun observe() {
        viewModel.shareData.observe(
            this,
            Observer { data ->
                run {
                    progressAdapter.submitList(data.resultList)
                    uri = FileUtils.saveImageToExternalFilesDir(
                        this,
                        binding.shareImage.drawable.toBitmap()
                    )
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.handler = this

        initView(binding)

        observe();
    }

    private fun initView(binding: ActivityShareBinding) {
        binding.shareImage.clipToOutline = true
        binding.progressList.adapter = progressAdapter

        val sample: ShareResult = ShareResult("새우버거 발닦기", "", "새우버거 발닦기 성공~\n더 친해지면 양치도 도전해보아요~~!")
        sample.resultList = mutableListOf(
            ShareResult.Result("포챠펭", 85),
            ShareResult.Result("비둘기", 12),
            ShareResult.Result("돼지", 3)
        )

        viewModel.updateShareData(sample)

        binding.fabShare.setOnClickListener {
            val data = viewModel.shareData.value

            val dialog = SnsDialog.newInstance(data!!.title, uri)
            dialog.show(supportFragmentManager, "SNS_DIALOG")
        }

        binding.fabSave.setOnClickListener {
            val data = viewModel.shareData.value

            val dialog = SaveDialog.newInstance(data!!.title)
            dialog.show(supportFragmentManager, "SAVE_DIALOG")
        }
    }
}