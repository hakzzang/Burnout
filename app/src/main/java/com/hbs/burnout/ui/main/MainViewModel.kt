package com.hbs.burnout.ui.main

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hbs.burnout.core.Event

class MainViewModel @ViewModelInject constructor() : ViewModel() {
    private val _startMission : MutableLiveData<Event<View>> = MutableLiveData()
    val startMission : LiveData<Event<View>> = _startMission

    fun passMissionActivity(view:View){
        _startMission.value = Event(view)
    }
}