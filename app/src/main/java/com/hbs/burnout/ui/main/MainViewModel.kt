package com.hbs.burnout.ui.main

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hbs.burnout.core.Event

class MainViewModel @ViewModelInject constructor() : ViewModel() {
    private val _startChatting : MutableLiveData<Event<View>> = MutableLiveData()
    val startChatting : LiveData<Event<View>> = _startChatting

    fun passChattingActivity(view:View){
        _startChatting.value = Event(view)
    }
}