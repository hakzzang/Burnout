package com.hbs.burnout.ui.chat

import android.os.Bundle
import android.view.View
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.databinding.FragmentCompletedStageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedStageFragment : BaseFragment<FragmentCompletedStageBinding>(){
    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentCompletedStageBinding = FragmentCompletedStageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}