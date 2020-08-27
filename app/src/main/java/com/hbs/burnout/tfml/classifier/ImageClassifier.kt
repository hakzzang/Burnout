package com.hbs.burnout.tfml.classifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.hbs.burnout.utils.BurnLog
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.label.Category
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.util.*
import kotlin.properties.Delegates


class ImageClassifier(ctx: Context) {
    private val result // depending on models architecture (possible multiple output)
            : Array<FloatArray>
    private val labels // list of all labels
            : ArrayList<String>
    private var imgData: ByteBuffer // models input format
    private val tflite // the model
            : Interpreter
    private val imagePixels = IntArray(DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH)
    var expectedIndex by Delegates.notNull<Int>()   // random label index which is to be drawn

    var find:Boolean = false

    fun classify(bitmap: Bitmap): CFResult {
        convertBitmapToByteBuffer(bitmap) // flatten bitmap to byte array
        tflite.run(imgData, result) // classify task
        return CFResult(result[0], labels) // create the result
    }

    fun classifyCategoryResult(bitmap: Bitmap, max: Int): List<Category> {
        convertBitmapToByteBuffer(bitmap) // flatten bitmap to byte array
        tflite.run(imgData, result) // classify task
        
        val out = mutableListOf<Category>()
        val result = CFResult(result[0], labels)

        for (index in result.topK) {
            val category = Category(getLabel(index), getProbability(index))
            out.add(category)
            if(expectedIndex == index && category.score > 0.6){
                find = true
            }
        }

        return out
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        if (imgData == null) {
            return
        }
        imgData!!.rewind()
        bitmap.getPixels(imagePixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until DIM_IMG_SIZE_WIDTH) {
            for (j in 0 until DIM_IMG_SIZE_HEIGHT) {
                val color = imagePixels[pixel++]
                imgData!!.putFloat(((color shr 16 and 0xFF) + (color shr 8 and 0xFF) + (color and 0xFF)) / 3.0f / 255.0f)
            }
        }
    }

    fun getProbability(index: Int): Float {
        return result[0][index]
    }

    fun getLabel(index: Int): String {
        return labels[index]
    }

    val numberOfClasses: Int
        get() = labels.size

    companion object {
        const val DIM_IMG_SIZE_HEIGHT = 28
        const val DIM_IMG_SIZE_WIDTH = 28
        private const val MODEL_FILE = "10/model10.tflite"
        private const val LABELS_FILE = "10/labels.csv"
        private const val DIM_BATCH_SIZE = 1
        private const val DIM_PIXEL_SIZE = 1
    }

    init {
        // load model
        val modelBuffered: MappedByteBuffer = ModelInput.loadModelFile(ctx, MODEL_FILE)
        BurnLog.Info(this, "load buffer: ${modelBuffered.isLoaded}")
        val option = Interpreter.Options()
        tflite = Interpreter(modelBuffered, option)

        // load labels
        labels = ModelInput.readLabels(ctx, LABELS_FILE) as ArrayList<String>
        result = Array(1) { FloatArray(labels.size) }

        // allocate memory for model input
        imgData = ByteBuffer.allocateDirect(4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH * DIM_PIXEL_SIZE)
        imgData.order(ByteOrder.nativeOrder())
        BurnLog.Info(this,"Successfully created a Tensorflow Lite sketch classifier.")
    }
}