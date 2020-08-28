package com.hbs.burnout.ui.ext.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hbs.burnout.R
import com.hbs.burnout.databinding.DialogGameEndingBinding

class EndingDialog : DialogFragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogGameEndingBinding.inflate(inflater, container, false)
        binding.btnEndingBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}