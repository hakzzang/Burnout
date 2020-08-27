package com.hbs.burnout.ui.share

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.hbs.burnout.core.BaseFragment
import androidx.lifecycle.Observer
import com.hbs.burnout.databinding.FragmentShareNormalBinding

class ShareNormalFragment : BaseFragment<FragmentShareNormalBinding>(){

    private val viewModel by activityViewModels<ShareViewModel>()
    private val progressAdapter = ProgressAdapter()

    private var uri: Uri? = null

    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentShareNormalBinding = FragmentShareNormalBinding.inflate(layoutInflater)

    private fun observe(){
        viewModel.shareData.observe(
            requireActivity(),
            Observer { data ->
                run {

                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initView()
        observe()
    }

    private fun initView() {
    }
}