package com.hbs.burnout.tfml.classifier

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*


/**
 * Helper class to read TensorFlow model and labels from file
 */
internal object ModelInput {
    /*
     * Reads the compressed model as MappedByteBuffer from file.
     *
     */
    @Throws(IOException::class)
    fun loadModelFile(ctx: Context, modelFile: String?): MappedByteBuffer {
        val assetManager = ctx.assets
        val fileDescriptor = assetManager.openFd(modelFile!!)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /*
     * Read labels from file to an array list of labels.
     * This list represents the mapping from index position to label.
     *
     */
    fun readLabels(ctx: Context, labelsFile: String): ArrayList<*> {
        val lineList = ArrayList<String>()
        return try {
            val inputStream = ctx.assets.open(labelsFile)

            inputStream.bufferedReader().forEachLine {
                lineList.add(it.split(",")[1])
            }
            inputStream.close()
            lineList

        } catch (ex: IOException) {
            throw IllegalStateException("Cannot read labels from $labelsFile $ex")
        }
    }
}
