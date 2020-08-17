package com.hbs.burnout.ui.share

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hbs.burnout.model.ShareResult

class ShareViewModel @ViewModelInject constructor() : ViewModel() {
    var _shareData:MutableLiveData<ShareResult> = MutableLiveData();
    val shareData:LiveData<ShareResult> = _shareData;

    fun updateShareData(data:ShareResult){
        _shareData.value = data;
    }
}