package com.hbs.burnout.ui.camera.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.databinding.FragmentPreviewBinding
import com.hbs.burnout.ui.ext.view.rotate
import com.hbs.burnout.ui.share.ShareActivity
import com.hbs.burnout.utils.ActivityNavigation
import com.hbs.burnout.utils.BurnLog
import com.hbs.burnout.utils.TransitionConfigure
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PreviewResultFragment : BaseFragment<FragmentPreviewBinding>() {
    private lateinit var outputDirectory: File

    override fun bindBinding(): FragmentPreviewBinding = FragmentPreviewBinding.inflate(layoutInflater)

    override fun isUseTransition(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bitmapFileUriPath = arguments?.getString("imageFileUriPath")?:return
        val rotationf = arguments?.getInt("targetRotation")?.toFloat()!!
        val imagetype = arguments?.getInt("typeImage",0)
        val bitmapImage = BitmapFactory.decodeFile(bitmapFileUriPath)
        BurnLog.Debug(this,"SaveFile=2 $bitmapFileUriPath")
        BurnLog.Debug(this, "rotation value:$rotationf")

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_camera_container).navigate(
                PreviewResultFragmentDirections.actionPreviewToCamera())
        }
        binding.analyzeButton.setOnClickListener {

            val out = FileOutputStream(bitmapFileUriPath)

            bitmapImage?.rotate(rotationf)?.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()

            val intent = Intent(this.context, ShareActivity::class.java)

            intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.LINEAR_TYPE)
            intent.putExtra("resultImagePath", bitmapFileUriPath)
            intent.putExtra("resultImageType", imagetype)
            Log.d("result-dada","start")

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
                BurnLog.Debug(requireActivity(), "result-dada:$result")
                when(result.resultCode){
                    ActivityNavigation.SHARE_TO_CHATTING -> {
                        requireActivity().setResult(result.resultCode, result.data)
                        requireActivity().finish()

                    }
                    ActivityNavigation.CAMERA_TO_CHATTING-> {
                        requireActivity().setResult(result.resultCode, result.data)
                        requireActivity().finish()
                    }
                }
            }.launch(intent)
        }

        binding.viewPreview.apply {
            setImageBitmap(bitmapImage?.rotate(rotationf))
        }
    }

    companion object {

        private const val TAG = "ShareActivity"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)
    }
}