package com.hbs.burnout.ui.share

import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogSnsBinding
import com.hbs.burnout.utils.IntentUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SnsDialog() : BottomSheetDialogFragment() {

    private val viewModel by viewModels<ShareViewModel>()

    private val snsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // do something here
        }


    var uri: Uri? = null
    var title: String? = ""

    companion object {
        private val EXTRA_TITLE = "EXTRA_TITLE"
        private val EXTRA_URI = "EXTRA_URI"

        fun newInstance(title: String, uri: Uri?): SnsDialog {
            val args = Bundle()
            args.putString(EXTRA_TITLE, title)
            args.putString(EXTRA_URI, uri.toString())

            val fragment = SnsDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private fun observe() {
        viewModel.snsType.observe(requireActivity(), Observer { type ->
            when (type) {
                SnsType.KAKAOTALK -> {
                    val intent = IntentUtils.createKakaotalkIntent(context, uri)
                    snsLauncher.launch(intent)
                }
                SnsType.FACEBOOK -> {
                    val result:String = "'$title' 미션을 완료했어요!\n\n #번아웃_챌린지 #$title"
                    val intent = IntentUtils.createFacebookIntent(context, result)
                    snsLauncher.launch(intent)
                }
                SnsType.INSTAGRAM -> {
                    val intent = IntentUtils.createInstagramFeedIntent(context, uri)
                    snsLauncher.launch(intent)
                }
            }
            dismiss()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(EXTRA_TITLE, "")
        uri = Uri.parse(arguments?.getString(EXTRA_URI, ""))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.apply {
            window?.setDimAmount(0.5f)
            setOnShowListener { dialog ->
                run {
                    val view = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
                    view?.setBackgroundColor(Color.parseColor("#00000000"))
                }
            }

            setCanceledOnTouchOutside(true)
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogSnsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel
        binding.handler = this

        observe()

        return binding.root
    }
}