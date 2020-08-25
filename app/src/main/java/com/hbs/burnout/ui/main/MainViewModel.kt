package com.hbs.burnout.ui.main

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hbs.burnout.core.Event
import com.hbs.burnout.domain.local.usecase.MainUseCase

class MainViewModel @ViewModelInject constructor(val mainUseCase: MainUseCase) : ViewModel() {
    private val _startChatting : MutableLiveData<Event<View>> = MutableLiveData()
    val startChatting : LiveData<Event<View>> = _startChatting

    val stages = liveData {
        val stages = mainUseCase.loadMission()
        emit(stages.toList())
    }

    fun passChattingActivity(view:View){
        _startChatting.value = Event(view)
    }
}