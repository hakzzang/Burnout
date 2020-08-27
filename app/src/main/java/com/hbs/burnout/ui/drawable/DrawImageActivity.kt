package com.hbs.burnout.ui.drawable

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.core.BaseActivity
import com.hbs.burnout.databinding.ActivityDrawBinding
import com.hbs.burnout.tfml.TFModelType
import com.hbs.burnout.tfml.classifier.ImageClassifier
import com.hbs.burnout.ui.share.ShareActivity
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.FileUtils
import com.hbs.burnout.utils.TransitionConfigure
import java.io.IOException
import java.util.*


class DrawImageActivity : BaseActivity<ActivityDrawBinding>() {
    private var classifier: ImageClassifier? = null
    private var isFind:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@DrawImageActivity
        }

        binding.paintView.initView()

        // instantiate classifier
        try {
            classifier = ImageClassifier(applicationContext)
            resetView()
        } catch (e: IOException) {
            Log.e("DrawImageActivity", "Cannot initialize tfLite model!", e)
            e.printStackTrace()
        } catch (e2: NullPointerException) {
            Log.e("DrawImageActivity", "Cannot initialize tfLite model!", e2)
        }
    }

    fun onClearClick(view: View?) {
        Log.i(TAG, "Clear sketch event triggers")
        binding.paintView.clear()
    }

    // scale original bitmap down to network size (28x28)
    fun getNormalizedBitmap(mBitmap: Bitmap): Bitmap {
        val scaleFactor: Float = ImageClassifier.DIM_IMG_SIZE_HEIGHT / mBitmap.height.toFloat()
        // todo: cut empty space around sketch
        return Bitmap.createScaledBitmap(
            mBitmap,
            (mBitmap.width * scaleFactor).toInt(),
            (mBitmap.height * scaleFactor).toInt(), true
        )
    }

    fun onDetectClick(view : View?) {
        Log.i(TAG, "Detect sketch event triggers")
        if (classifier == null) {
            Log.e(TAG, "Cannot initialize tfLite model!")
        } else {
            val sketch = binding.paintView.normalizedBitmap
            val orgBitmap:Bitmap? = binding.paintView.getmBitmap()
            val intent = Intent(baseContext, ShareActivity::class.java)

            if (orgBitmap != null) {

                val file = FileUtils.getOrMakeRecognizeFile(this)
                val file2 = FileUtils.getOrMakeRecognizeFile2(this)
                FileUtils.saveBitmapToFile(file, orgBitmap)
                FileUtils.saveBitmapToFile(file2, sketch)

                intent.putExtra(
                    TransitionConfigure.TRANSITION_TYPE,
                    TransitionConfigure.LINEAR_TYPE
                )
                intent.putExtra("resultImagePath", file.toUri().path)
                intent.putExtra("resultImageType", TFModelType.SCETCHI.ordinal)
                intent.putExtra("expectItemName", classifier?.getLabel(classifier!!.expectedIndex))
                intent.putExtra("expectedIndex", classifier!!.expectedIndex)
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
                    Log.d("result-dada",result.toString())
                    when(result.resultCode){
                        ActivityNavigation.SHARE_TO_CHATTING -> {
                            setResult(result.resultCode, result.data)
                            finish()

                        }
                        ActivityNavigation.DRAWING_TO_CHATTING-> {
                            setResult(result.resultCode, result.data)
                            finish()
                        }
                    }
                }.launch(intent)
            }
//            showImage(binding.paintView.scaleBitmap(40, sketch));
        }
    }

    fun onNextClick(view: View?) {
        resetView()
    }

    // debug: ImageView with rescaled 28x28 bitmap
    private fun showImage(bitmap: Bitmap) {
        val builder = Dialog(this)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        builder.setOnDismissListener { }
        val imageView = ImageView(this)
        imageView.setImageBitmap(bitmap)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        builder.show()
    }

    private fun resetView() {
        binding.activityDraw.setBackgroundColor(Color.WHITE)
        binding.paintView.clear()
        binding.txtResultLabel.text = ""

        // get a random label and set as expected class
        classifier?.expectedIndex = Random().nextInt(classifier!!.numberOfClasses)
        binding.txtDrawLabel.text = classifier?.getLabel(classifier!!.expectedIndex) + "(을/를) 그려보자!"
    }


    override fun bindBinding() = ActivityDrawBinding.inflate(layoutInflater)

    override fun isUseTransition() = false

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

    override fun onBackPressed() {
        setResult(ActivityNavigation.DRAWING_TO_CHATTING)
        super.onBackPressed()
    }

    companion object {
        val TAG = this.javaClass.name
    }
}