package com.hbs.burnout.ui.save

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogSaveBinding
import com.hbs.burnout.ui.share.ShareViewModel
import java.util.*


class SaveDialog() : DialogFragment() {

    var title: String? = ""

    companion object {
        private val EXTRA_TITLE = "EXTRA_TITLE"

        fun newInstance(title: String): SaveDialog {
            val args = Bundle()
            args.putString(EXTRA_TITLE, title)

            val fragment = SaveDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: DialogSaveBinding
    private val shareViewModel by viewModels<ShareViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(EXTRA_TITLE, "")?.replace(" ", "_")
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

        binding.tagTitle.text = resources.getString(R.string.tag_title, title)

        return binding.root
    }

    private fun takeScreenshot() {
        val date = Date()
    }

    private fun saveImage() {
        val bitmap = binding.saveImg.getDrawable().toBitmap()
        val values = ContentValues()
        with(values) {
            put(MediaStore.Images.Media.TITLE, title)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/my_folder")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val uri = context?.getContentResolver()
            ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val fos = context?.contentResolver?.openOutputStream(uri!!)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos?.run {
            flush()
            close()
        }

    }
}