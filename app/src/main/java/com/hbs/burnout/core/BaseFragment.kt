package com.hbs.burnout.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment <B:ViewDataBinding>: Fragment(){
    lateinit var binding: B

    abstract fun isUseTransition(): Boolean

    abstract fun bindBinding(): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = bindBinding()
        binding.lifecycleOwner = this
        startPostponedEnterTransition()
        return binding.root
    }
}