package com.hbs.burnout.ui.share

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hbs.burnout.model.ShareResult

class ShareViewModel @ViewModelInject constructor() : ViewModel() {
    var _shareData:MutableLiveData<ShareResult> = MutableLiveData()
    val shareData:LiveData<ShareResult> = _shareData

    var _snsType:MutableLiveData<SnsType> = MutableLiveData()
    val snsType:LiveData<SnsType> = _snsType

    var tagSelected:MutableLiveData<Boolean> = MutableLiveData(true)


    fun updateShareData(data:ShareResult){
        _shareData.value = data;
    }

    fun shareToSns(type:SnsType){
        _snsType.value = type
    }

    fun updateTagSelected(isSelected:Boolean){
        tagSelected.value = isSelected
    }
}

enum class SnsType{
    KAKAOTALK, FACEBOOK, INSTAGRAM
}

