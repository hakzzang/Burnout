package com.hbs.burnout.ui.save

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogSaveBinding
import com.hbs.burnout.ui.share.ShareViewModel
import com.hbs.burnout.utils.FileUtils
import com.hbs.burnout.utils.FileUtils.OnDownloadListener
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "SaveDialog"

@AndroidEntryPoint
class SaveDialog() : DialogFragment(), OnDownloadListener {
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val fileName = "BurnOut_${System.currentTimeMillis()}}"
                getBitmapFromView(binding.tagContainer)?.let { bitmap ->
                    FileUtils.run {
                        context?.let { it1 ->
                            saveImageToMediaStore(
                                it1,
                                bitmap,
                                fileName,
                                this@SaveDialog
                            )
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "쓰기 권한이 없으므로 다운로드를 할 수 없어요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dismiss()
        }


    private lateinit var binding: DialogSaveBinding
    private val shareViewModel by activityViewModels<ShareViewModel>()

    companion object {
        fun newInstance(): SaveDialog {
            val args = Bundle()
            val fragment = SaveDialog()
            fragment.arguments = args
            return fragment
        }
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
            binding.saveImg.setImageURI(Uri.parse(it.uri))
        }

        return binding.root
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun saveImage() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                val fileName = "BurnOut_${System.currentTimeMillis()}}"
                getBitmapFromView(binding.tagContainer)?.let { bitmap ->
                    FileUtils.run {
                        context?.let { it1 ->
                            saveImageToMediaStore(
                                it1,
                                bitmap,
                                fileName,
                                this@SaveDialog
                            )
                        }
                    }
                }
                dismiss()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                Toast.makeText(
                    requireContext(),
                    "쓰기 권한이 없으면 다운로드를 할 수 없어요. 꼭 확인해주세요~",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }

    }

    override fun onSuccess(uri: Uri) {
        Toast.makeText(context, "다운로드 완료!", Toast.LENGTH_SHORT).show()
    }

    override fun onFailed(error: String?) {
        Toast.makeText(context, "다운로드 실패..!", Toast.LENGTH_SHORT).show()
    }
}