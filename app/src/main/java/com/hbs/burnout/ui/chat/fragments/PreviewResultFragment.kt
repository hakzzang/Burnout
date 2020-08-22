package com.hbs.burnout.ui.mission.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hbs.burnout.R
import com.hbs.burnout.databinding.FragmentPreviewBinding
import com.hbs.burnout.ui.share.ShareActivity
import com.hbs.burnout.utils.TransitionConfigure

class PreviewResultFragment : Fragment() {
    private  lateinit var binding: FragmentPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_preview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bitmapImage = arguments?.getParcelable<Bitmap>("resultImage")
        val rotationf = arguments?.getInt("targetRotation")?.toFloat()!!

        Log.i("PreviewResultFragment", "rotation value:$rotationf")

        binding = FragmentPreviewBinding.bind(view)
        binding.cancelButton.setOnClickListener {
            Log.i("PREVIEW", "취소취소!! 카메라로 돌아가자!!")
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                PreviewResultFragmentDirections.actionPreviewToCamera())
        }
        binding.analyzeButton.setOnClickListener {
            Log.i("PREVIEW", "결과 화면으로 가자가자! ")
            val intent = Intent(this.context, ShareActivity::class.java)
//        val intent = Intent(itemView.context, CameraMissionActivity::class.java)
            intent.putExtra(TransitionConfigure.TRANSITION_TYPE, TransitionConfigure.LINEAR_TYPE)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.viewPreview.apply {
            setImageBitmap(bitmapImage?.rotate(rotationf))
        }
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

}