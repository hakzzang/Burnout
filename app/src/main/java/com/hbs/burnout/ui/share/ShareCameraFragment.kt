package com.hbs.burnout.ui.share

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.hbs.burnout.core.BaseFragment
import androidx.lifecycle.Observer
import com.hbs.burnout.databinding.FragmentShareCameraBinding
import com.hbs.burnout.utils.FileUtils

class ShareCameraFragment : BaseFragment<FragmentShareCameraBinding>(){

    private val viewModel by activityViewModels<ShareViewModel>()
    private val progressAdapter = ProgressAdapter()

    private var uri: Uri? = null

    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentShareCameraBinding = FragmentShareCameraBinding.inflate(layoutInflater)

    private fun observe(){
        viewModel.shareData.observe(
            requireActivity(),
            Observer { data ->
                Log.d("shareTest", "[ShareCameraFragment] observe() : data = "+data.title)
                run {
                    progressAdapter.submitList(data.resultList)

                    data.image?.let {
                        binding.shareImage.setImageBitmap(it)
                    }

                    uri = FileUtils.saveImageToExternalFilesDir(
                        context,
                        binding.shareImage.drawable.toBitmap()
                    )
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
        binding.shareImage.clipToOutline = true
        binding.progressList.adapter = progressAdapter
    }
}