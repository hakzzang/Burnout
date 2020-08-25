package com.hbs.burnout.ui.main

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hbs.burnout.core.Event
import com.hbs.burnout.domain.local.usecase.MainUseCase
import com.hbs.burnout.model.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(val mainUseCase: MainUseCase) : ViewModel() {
    private val _startChatting: MutableLiveData<Event<View>> = MutableLiveData()
    val startChatting: LiveData<Event<View>> = _startChatting

    private val _stage = MutableLiveData<List<Stage>>()
    val stages: LiveData<List<Stage>> = _stage

    fun passChattingActivity(view: View) {
        _startChatting.value = Event(view)
    }

    fun selectStages() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mainUseCase.loadMission()
            _stage.postValue(result)
        }
    }
}