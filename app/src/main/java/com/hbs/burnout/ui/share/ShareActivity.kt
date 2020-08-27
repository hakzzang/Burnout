package com.hbs.burnout.ui.share


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityShareBinding
import com.hbs.burnout.model.EventType
import com.hbs.burnout.model.ShareResult
import com.hbs.burnout.tfml.TFModelType
import com.hbs.burnout.tfml.TFModelWorker
import com.hbs.burnout.ui.save.SaveDialog
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.FileUtils
import org.tensorflow.lite.support.label.Category

internal const val MAX_RESULT_DISPLAY = 3 // Maximum number of results displayed

class ShareActivity : BaseActivity<ActivityShareBinding>() {
    private var resultType: Int = 0
    private var uri: Uri? = null

    private var bitmapImage: Bitmap? = null

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
        viewModel.missionComplete.observe(this, Observer { isComplete ->
            if (isComplete) {
                binding.fabNext.apply {
                    setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                    text = "미션 완료"
                    setOnClickListener {
                        val intent = makeSuccessResultIntent()
                        setResult(ActivityNavigation.SHARE_TO_CHATTING, intent)
                        finish()
                    }
                }
            } else {
                binding.fabNext.apply {
                    setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                    text = "미션 실패"
                    setOnClickListener {
                        val intent = makeFailResultIntent()
                        setResult(ActivityNavigation.SHARE_TO_CHATTING, intent)
                        finish()
                    }
                }
            }
        })
    }

    lateinit var bitmapImagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ShareActivity
            viewModel = this@ShareActivity.viewModel
            handler = this@ShareActivity
        }

        this.bitmapImagePath = intent.getStringExtra("resultImagePath").toString()
        this.resultType = intent.getIntExtra("resultImage", 1)

        bitmapImagePath.let {
            Log.d(TAG, "image path:" + bitmapImagePath)
            this.bitmapImage = BitmapFactory.decodeFile(it)
            uri = bitmapImage?.let {
                FileUtils.saveImageToExternalFilesDir(
                    this,
                    it
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShareCameraFragment())
            .commit()

        initView(binding)

        observe()
    }

    override fun onStart() {
        super.onStart()
        if (bitmapImage != null) {
            runTFImageParser(bitmapImage!!)
        }
    }

    private fun runTFImageParser(imageBitmap: Bitmap) {
        val tfWork = TFModelWorker()

        when (resultType) {
            TFModelType.BIRD.ordinal -> tfWork.initModel(baseContext, TFModelType.BIRD)
            else -> tfWork.initModel(baseContext, TFModelType.ANYTHING)
        }

        val outputs = tfWork.alnalyze(imageBitmap)
        val items = mutableListOf<ShareResult.Result>()

        val isCompleteAnalysis = isComplete(outputs)
        val completeMsg = if (isCompleteAnalysis) {
            "성공!\n다음 미션에 도전해 보아요!"
        } else {
            "실패~\n다시 도전해보아요~~!"
        }

        val title = when (resultType) {
            TFModelType.BIRD.ordinal -> "이것은 새인가!"
            else -> "이것을 찍은게 맞나요?"
        }

        var sample = ShareResult(title, imageBitmap, completeMsg)
        sample.eventType = EventType.CAMERA

        for (output in outputs) {
            Log.i(TAG, "label:${output.label} , score:${output.score}")
            items.add(ShareResult.Result(output.label, (output.score * 100).toInt()))
        }

        sample.resultList = items

        viewModel.updateShareData(sample)
        viewModel.setMissionComplete(isCompleteAnalysis)
    }

    private fun initView(binding: ActivityShareBinding) {
        binding.fabShare.setOnClickListener {
            val data = viewModel.shareData.value

            val dialog = SnsDialog.newInstance(data!!.title, uri)
            dialog.show(supportFragmentManager, "SNS_DIALOG")
        }

        binding.fabSave.setOnClickListener {
            val dialog = SaveDialog.newInstance()
            dialog.show(supportFragmentManager, "SAVE_DIALOG")
        }

        binding.fabNext.setOnClickListener {

        }
    }

    private fun isComplete(outputs: List<Category>): Boolean {
        return outputs[0].label != "None" && (outputs[0].score * 100) >= 30
    }

    private fun makeSuccessResultIntent(): Intent {
        val intent = Intent()
        uri = bitmapImage?.let {
            FileUtils.saveImageToExternalFilesDir(this, it)
        }
        intent.putExtra(ActivityNavigation.ANALYZE_RESULT, uri?.path)
        intent.putExtra(ActivityNavigation.ANALYZE_IS_COMPLETE, true)
        return intent
    }

    private fun makeFailResultIntent(): Intent {
        val intent = Intent()
        intent.putExtra(ActivityNavigation.ANALYZE_IS_COMPLETE, false)
        return intent
    }

    companion object {
        const val TAG = "ShareActivity"
    }
}