package com.hbs.burnout.ui.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbs.burnout.core.Event
import com.hbs.burnout.domain.local.usecase.ChattingUseCase
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.StageProgress
import com.hbs.burnout.utils.script.MissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChattingViewModel @ViewModelInject constructor(
    private val chattingUseCase: ChattingUseCase
) : ViewModel() {

    private val _readingScript = MutableLiveData<Event<Script>>()
    val readingScript:LiveData<Event<Script>> = _readingScript

    private val _parsedScript = MutableLiveData<Event<List<Script>>>()
    val parsedScript : LiveData<Event<List<Script>>> = _parsedScript

    private val _completedReadingScript = MutableLiveData<Event<Script>>()
    val completedReadingScript : LiveData<Event<Script>> = _completedReadingScript

    private val _completedStage = MutableLiveData<Event<Unit>>()
    val completedStage : LiveData<Event<Unit>> =_completedStage

    fun clearScriptCache() {
        chattingUseCase.clearScriptCache()
    }

    fun readNextScriptLine(scriptNumber: Int) {
        val script = chattingUseCase.readNextScriptLine(scriptNumber) {
            _completedStage.value = Event(Unit)
        }?:return
        _readingScript.value = Event(script)
    }

    fun emitParsingScript(newScript: Script) {
        viewModelScope.launch(Dispatchers.IO) {
            chattingUseCase.readScriptLine(newScript, { scriptCache ->
                _parsedScript.value = Event(scriptCache)
            }, { lastScript ->
                chattingUseCase.saveScript(lastScript)
                viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main){
                    _completedReadingScript.value = Event(lastScript)
                }
            })
        }
    }

    fun selectAnswer(answerNumber: Int){
        chattingUseCase.setSelectedAnswer(answerNumber)
        viewModelScope.launch(Dispatchers.IO){
            chattingUseCase.answerScriptLine(answerNumber, { scriptCache ->
                _parsedScript.value = Event(scriptCache)
            },{lastScript->
                chattingUseCase.saveScript(lastScript)
                viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main){
                    _completedReadingScript.value = Event(lastScript)
                }
            })
        }
    }

    fun takePicture(){
        viewModelScope.launch(Dispatchers.IO){
            chattingUseCase.takePictureScriptLine( { scriptCache ->
                _parsedScript.value = Event(scriptCache)
            },{lastScript->
                chattingUseCase.saveScript(lastScript)
                viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main){
                    _completedReadingScript.value = Event(lastScript)
                }
            })
        }
    }

    fun drawingImage(){
        viewModelScope.launch(Dispatchers.IO){
            chattingUseCase.drawingImageScriptLine( { scriptCache ->
                _parsedScript.value = Event(scriptCache)
            },{lastScript->
                chattingUseCase.saveScript(lastScript)
                viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main){
                    _completedReadingScript.value = Event(lastScript)
                }
            })
        }
    }

    fun loadStage(scriptNumber: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val script = chattingUseCase.loadScriptOf(scriptNumber)
            withContext(Dispatchers.Main){
                _parsedScript.value = Event(script)
                if(script.isNotEmpty()){
                    viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main){
                        _completedReadingScript.value = Event(script.last())
                    }
                }else{
                    readNextScriptLine(scriptNumber)
                }
            }
        }
    }

    fun startStage(stageRound: Int,dbCallback:()->Unit){
        viewModelScope.launch(Dispatchers.IO){
            val stage = MissionHelper.getStage(stageRound)
            stage.progress = StageProgress.PLAYING
            chattingUseCase.saveStage(stage)
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main) { dbCallback() }
        }
    }

    fun completeStage(stageRound:Int, dbCallback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            val stage = MissionHelper.getStage(stageRound)
            stage.progress = StageProgress.COMPLETED
            chattingUseCase.saveStage(stage)
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.Main) { dbCallback() }
        }
    }
}