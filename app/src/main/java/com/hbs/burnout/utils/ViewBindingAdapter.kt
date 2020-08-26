package com.hbs.burnout.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

object ViewBindingAdapter {
    @BindingAdapter("app:setSelected")
    @JvmStatic
    fun setSelected(view: View, isSelected:MutableLiveData<Boolean>){
        view.isSelected = isSelected.value!!
    }
}