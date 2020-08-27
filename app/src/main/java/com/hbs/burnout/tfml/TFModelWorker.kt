package com.hbs.burnout.tfml

import android.content.Context
import android.graphics.Bitmap
import com.hbs.burnout.ml.Anything
import com.hbs.burnout.ml.BirdModel
import com.hbs.burnout.tfml.TFModelType.*
import com.hbs.burnout.tfml.classifier.ImageClassifier
import com.hbs.burnout.ui.share.MAX_RESULT_DISPLAY
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category

enum class TFModelType {
    BIRD, //새를 찍는다
    ANYTHING,  // 기본 이미지 분류 (아무거나 찍고 맞춰보자 미션으로 진행해보기 위해 추가)
    SCETCHI,
}

class TFModelWorker {
    var find: Boolean = false
    private lateinit var model:Any
    private lateinit var context:Context
    private lateinit var mltype:TFModelType

    fun initModel(ctx: Context, type:TFModelType, expectedIndex:Int = 0){
        // 에물레이터로 하다 보니 이 옵션은 꺼야만 돌아감. 실제 단말에서 테스트 및 애뮬 설정을 맞췄을 경우에 활성화 필요
        //val options = Model.Options.Builder().setDevice(Model.Device.GPU).build()
        context = ctx
        mltype = type
        when(type) {
            BIRD -> this.model = BirdModel.newInstance(ctx)//,options)
            ANYTHING -> this.model = Anything.newInstance(ctx)//,options)
            SCETCHI -> this.model = ImageClassifier(ctx).apply {
                this.expectedIndex = expectedIndex
            }
        }
    }

    fun alnalyze (imageBitmap: Bitmap): List<Category> {
        val tfImage = TensorImage.fromBitmap(imageBitmap)
        val outputs = when(mltype) {
                BIRD -> {
                    (model as BirdModel).process(tfImage)
                            .probabilityAsCategoryList.apply {
                                sortByDescending { it.score } // Sort with highest confidence first
                            }.take(MAX_RESULT_DISPLAY) // take the top results
                }
                ANYTHING -> {
                    (model as Anything).process(tfImage)
                            .probabilityAsCategoryList.apply {
                                sortByDescending { it.score } // Sort with highest confidence first
                            }.take(MAX_RESULT_DISPLAY) // take the top results
                }
                SCETCHI -> {
                    (model as ImageClassifier).classifyCategoryResult(imageBitmap,
                        MAX_RESULT_DISPLAY)
                }
        }

        when (mltype) {
            BIRD -> (model as BirdModel).close()
            ANYTHING -> (model as Anything).close()
            SCETCHI -> this.find = (model as ImageClassifier).find
        }

        return outputs
    }
}
