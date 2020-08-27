package com.hbs.burnout.ui.ext.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogDrawingImageBinding
import com.hbs.burnout.databinding.DialogTakePictureBinding

class DrawingImageDialog(
    private val clickCallback: (DialogFragment) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogDrawingImageBinding.inflate(layoutInflater, container, false)
        binding.btnDrawImage.setOnClickListener{
            clickCallback(this)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let{
            val bottomSheetDialog = dialog as BottomSheetDialog
            bottomSheetDialog.behavior.isDraggable= false
        }
    }
}