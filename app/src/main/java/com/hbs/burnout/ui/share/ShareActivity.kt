package com.hbs.burnout.ui.share

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityShareBinding
import com.hbs.burnout.ml.BirdModel
import com.hbs.burnout.model.EventType
import com.hbs.burnout.model.ShareResult
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model


private const val MAX_RESULT_DISPLAY = 3 // Maximum number of results displayed

class ShareActivity : BaseActivity<ActivityShareBinding>() {
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

    lateinit var bitmapImagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.bitmapImagePath = intent.getStringExtra("resultImagePath").toString()

        bitmapImagePath.let {
            Log.d(TAG, "image path:" + bitmapImagePath)
            this.bitmapImage = BitmapFactory.decodeFile(it)
        }

        binding.shareImage.setImageBitmap(bitmapImage)
        binding.viewModel = viewModel
        binding.handler = this

        initView(binding)
        observe();
    }

    override fun onStart() {
        super.onStart()
        if (bitmapImage != null) {
            alnalyzer(bitmapImage!!)
        }
    }

    private fun alnalyzer (imageBitmap: Bitmap) {

        val options = Model.Options.Builder().setDevice(Model.Device.GPU).build()
        val birdModel = BirdModel.newInstance(baseContext, options)
        val items = mutableListOf<ShareResult.Result>()
        val tfImage = TensorImage.fromBitmap(imageBitmap)
        val outputs = birdModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score } // Sort with highest confidence first
            }.take(MAX_RESULT_DISPLAY) // take the top results

        val completeMsg = if (outputs[0].label.equals("None") || (outputs[0].score*100) < 30) {
            "실패~\n" +
                    "다시 도전해보아요~~!"
        } else {
            "성공!\n 다음 미션에 도전해 보아요!"
        }

        var sample = ShareResult("이것은 새인가?", bitmapImagePath, completeMsg)
        sample.eventType = EventType.CAMERA

        for (output in outputs) {
            Log.i(TAG, "label:${output.label} , score:${output.score}")
            items.add(ShareResult.Result(output.label, (output.score*100).toInt()))
        }

        sample.resultList = items

        viewModel.updateShareData(sample)
    }

    private fun initView(binding: ActivityShareBinding) {
        binding.shareImage.clipToOutline = true
        binding.progressList.adapter = progressAdapter

        val sample:ShareResult = ShareResult("새우버거 발닦기", "", "새우버거 발닦기 성공~\n더 친해지면 양치도 도전해보아요~~!")
        sample.resultList = mutableListOf(ShareResult.Result("포챠펭", 85), ShareResult.Result("비둘기", 12), ShareResult.Result("돼지", 3))

        viewModel.updateShareData(sample)
    }

    private fun observe(){
        viewModel.shareData.observe(this, Observer { data-> progressAdapter.submitList(data.resultList)})
    }

    companion object {
        const val TAG = "ShareActivity"
    }
}