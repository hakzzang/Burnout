package com.hbs.burnout.tfml.classifier

import android.util.Log
import org.tensorflow.lite.support.label.Category
import java.util.*


/**
 * Readable representation of the models output vector.
 * Contains label, label position in result vector and probability.
 */
class CFResult(result: FloatArray, labels: ArrayList<String>) {
    val label: String
    val labelPosition: Int
    val probbability: Float
    val topK // contains the index
            : List<Int>

    // find the index with the maximum probability
    private fun argmax(result: FloatArray): Int {
        var maxProb = 0.0f
        var maxIndex = -1
        for (i in result.indices) {
            if (result[i] > maxProb) {
                maxProb = result[i]
                maxIndex = i
            }
        }
        if (maxIndex == -1) {
            Log.e("Result class", "argmax found no maximum")
        }
        return maxIndex
    }

    // returns the top k labels with probability
    private fun getTopkLabels(k: Int, result: FloatArray): List<Int> {
        val topK: MutableList<Int> = LinkedList()
        for (kk in 0 until k) {
            var maxProb = 0.0f
            var maxIndex = -1
            for (i in result.indices) {
                if (!topK.contains(i)) {
                    if (result[i] > maxProb) {
                        maxProb = result[i]
                        maxIndex = i
                    }
                }
            }
            topK.add(maxIndex)
        }
        return topK
    }

    init {
        labelPosition = argmax(result) // set index position
        probbability = result[labelPosition] // set probability
        label = labels[labelPosition] // search for label
        topK = getTopkLabels(3, result)
    }
}
