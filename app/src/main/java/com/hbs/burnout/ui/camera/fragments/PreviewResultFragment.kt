package com.hbs.burnout.ui.camera.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.databinding.FragmentPreviewBinding
import com.hbs.burnout.ui.ext.view.rotate
import com.hbs.burnout.ui.mission.CameraMissionActivity
import com.hbs.burnout.ui.share.ShareActivity
import com.hbs.burnout.utils.ActivityNavigation
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
        Log.d("SaveFile=2",bitmapFileUriPath)
        Log.i("PreviewResultFragment", "rotation value:$rotationf")

        binding.cancelButton.setOnClickListener {
            Log.i("PREVIEW", "취소취소!! 카메라로 돌아가자!!")
            Navigation.findNavController(requireActivity(), R.id.fragment_camera_container).navigate(
                PreviewResultFragmentDirections.actionPreviewToCamera())
        }
        binding.analyzeButton.setOnClickListener {
            Log.i("PREVIEW", "결과 화면으로 가자가자! ")
//            outputDirectory = CameraMissionActivity.getOutputDirectory(requireContext())

            val out = FileOutputStream(bitmapFileUriPath)

            bitmapImage?.rotate(rotationf)?.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()

            val intent = Intent(this.context, ShareActivity::class.java)

            intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.LINEAR_TYPE)
            intent.putExtra("resultImagePath", bitmapFileUriPath)
            intent.putExtra("resultImageType", imagetype)
            Log.d("result-dada","start")

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
                Log.d("result-dada",result.toString())
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