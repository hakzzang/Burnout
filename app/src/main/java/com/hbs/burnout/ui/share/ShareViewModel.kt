package com.hbs.burnout.ui.share

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hbs.burnout.domain.local.usecase.ShareUseCase
import com.hbs.burnout.model.ShareResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareViewModel @ViewModelInject constructor(
    private val shareUseCase: ShareUseCase
)  : ViewModel() {
    var _shareData: MutableLiveData<ShareResult> = MutableLiveData()
    val shareData: LiveData<ShareResult> = _shareData

    var _snsType: MutableLiveData<SnsType> = MutableLiveData()
    val snsType: LiveData<SnsType> = _snsType

    fun getShareResult(stageRound:Int) = liveData {
        emit(shareUseCase.loadShareData(stageRound))
    }

    var tagSelected: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _missionComplete = MutableLiveData(false)
    val missionComplete: LiveData<Boolean> = _missionComplete

    fun setMissionComplete(isComplete:Boolean, shareResult:ShareResult){
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            shareUseCase.saveShareData(shareResult)
            _missionComplete.postValue(isComplete)
        }
    }

    fun updateShareData(data: ShareResult) {
        _shareData.value = data;
    }

    fun shareToSns(type: SnsType) {
        _snsType.value = type
    }

    fun updateTagSelected(isSelected: Boolean) {
        tagSelected.value = isSelected
    }
}

enum class SnsType {
    KAKAOTALK, FACEBOOK, INSTAGRAM
}

