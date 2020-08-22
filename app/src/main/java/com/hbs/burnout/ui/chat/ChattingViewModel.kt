package com.hbs.burnout.ui.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbs.burnout.core.Event
import com.hbs.burnout.domain.local.usecase.ChattingUseCase
import com.hbs.burnout.model.Script
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChattingViewModel @ViewModelInject constructor(
    private val chattingUseCase: ChattingUseCase
) : ViewModel() {

    private val _readingScript = MutableLiveData<Event<Script>>()
    val readingScript:LiveData<Event<Script>> = _readingScript

    private val _parsedScript = MutableLiveData<List<Script>>(listOf())
    val parsedScript : LiveData<List<Script>> = _parsedScript

    private val _completedReadingScript = MutableLiveData<Event<Script>>()
    val completedReadingScript : LiveData<Event<Script>> = _completedReadingScript

    private val _completedStage = MutableLiveData<Event<Unit>>()
    val completedStage : LiveData<Event<Unit>> =_completedStage

    fun readNextScriptLine(scriptNumber: Int) {
        val script = chattingUseCase.readNextScriptLine(scriptNumber) {
            _completedStage.value = Event(Unit)
        }?:return
        _readingScript.value = Event(script)
    }

    fun emitParsingScript(newScript: Script) {
        viewModelScope.launch(Dispatchers.IO) {
            chattingUseCase.readScriptLine(newScript, { scriptCache ->
                _parsedScript.value = scriptCache
            },{lastScript->
                _completedReadingScript.postValue(Event(lastScript))
            })
        }
    }

    fun selectAnswer(answerNumber: Int){
        viewModelScope.launch(Dispatchers.IO){
            chattingUseCase.answerScriptLine(answerNumber, { scriptCache ->
                _parsedScript.value = scriptCache
            },{lastScript->
                _completedReadingScript.postValue(Event(lastScript))
            })
        }
    }
}