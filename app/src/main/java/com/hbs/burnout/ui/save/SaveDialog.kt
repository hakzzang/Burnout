package com.hbs.burnout.ui.save

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogSaveBinding
import com.hbs.burnout.ui.share.ShareViewModel
import com.hbs.burnout.utils.FileUtils
import java.util.*

const val TAG = "SaveDialog"

class SaveDialog() : DialogFragment() {

    private lateinit var binding: DialogSaveBinding
    private val shareViewModel by activityViewModels<ShareViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSaveBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = shareViewModel
            handler = this@SaveDialog
        }

        shareViewModel.shareData.value?.let {
            binding.tagTitle.text = resources.getString(R.string.tag_title, it.title)
            binding.saveImg.setImageBitmap(it.image)
        }

        binding.saveBtn.setOnClickListener{
            val bitmap = binding.saveImg.getDrawable().toBitmap()
            context?.let { it -> FileUtils.saveImageToMediaStore(it, bitmap, "BurnOut_test.jpeg") }
        }

        return binding.root
    }

    private fun takeScreenshot() {
        val date = Date()
    }
}