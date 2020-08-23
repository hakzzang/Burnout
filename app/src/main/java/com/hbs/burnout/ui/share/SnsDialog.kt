package com.hbs.burnout.ui.share

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hbs.burnout.databinding.DialogSnsBinding

class SnsDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<ShareViewModel>()

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

        binding.viewModel = viewModel
        binding.handler = this

        return binding.root
    }
}