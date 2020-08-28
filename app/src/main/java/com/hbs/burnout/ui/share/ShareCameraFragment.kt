package com.hbs.burnout.ui.share

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.databinding.FragmentShareCameraBinding
import com.hbs.burnout.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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
                run {
                    progressAdapter.submitList(data.resultList)

                    data.uri?.let {
                        binding.shareImage.setImageURI(Uri.parse(it))
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

    fun observeShareResult(stageRound:Int){
        lifecycleScope.launchWhenResumed {
            viewModel.getShareResult(stageRound).observe(viewLifecycleOwner, Observer {
                binding.shareImage.setImageURI(Uri.parse(it.uri))
                progressAdapter.submitList(it.resultList)
            })
        }
    }
}