package com.hbs.burnout.ui.mission

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hbs.burnout.model.Chatting
import com.hbs.burnout.utils.ScriptManager
import com.hbs.burnout.utils.ScriptManagerImpl
import kotlinx.coroutines.Dispatchers

class MissionViewModel @ViewModelInject constructor() : ViewModel() {
    private val scriptManager: ScriptManager = ScriptManagerImpl()

    fun makeScriptLiveData(missionNumber: Int) =
        liveData<List<Chatting>>(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            scriptManager.playScript(missionNumber, this)
        }
}