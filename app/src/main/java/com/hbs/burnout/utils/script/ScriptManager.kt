package com.hbs.burnout.utils.script

import android.util.Log
import com.hbs.burnout.model.EventType
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.ScriptBuilder
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

object ScriptConfiguration {
    const val LINE_ENTER_SPEED = 100L
    const val READING_SPEED = 85L
}

@FragmentScoped
interface ScriptManager {
    fun readNextScriptLine(scriptPageNumber: Int, completedStageCallback: () -> Unit): Script?

    //scriptCallback은 Chatting 클래스에서 대본의 정보를 모두 담고 있고,
    //String을 통해서 speed에 따른 말하는 내용이 담겨져 있습니다.
    fun clearCache()
    fun resetScript()
    fun pauseScript()
    fun loadScript(scriptNumber: Int, loadedScripts: List<Script>): List<Script>
    fun getCache(): MutableList<Script>
    fun setSelectedAnswer(answerNumber: Int)

    suspend fun readScriptLine(
        newScript: Script,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script>

    suspend fun answerScriptLine(
        answerNumber: Int,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): MutableList<Script>

    suspend fun takePictureScriptLine(
        isCompleted: Boolean,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script>

    suspend fun drawingImageScriptLine(
        isCompleted: Boolean,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script>
}

class ScriptManagerImpl @Inject constructor(private val scriptStorage: ScriptStorage) :
    ScriptManager {
    private var pageNumber = 0
    private var isStopScript = true
    private var selectedScriptNumber = -1
    private var selectedAnswerNumber = 0
    private val scriptCache = mutableListOf<Script>()

    override fun getCache(): MutableList<Script> = scriptCache

    override fun setSelectedAnswer(answerNumber: Int) {
        this.selectedAnswerNumber = answerNumber
    }

    override fun clearCache() {
        scriptCache.clear()
    }

    override suspend fun readScriptLine(
        newScript: Script,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script> {
        var newWord = ""
        val newScript = newScript.parse()
        val (user, message, event, stage, id) = newScript
        if (newScript.eventType == EventType.CHATTING) {
            message.toCharArray().forEachIndexed { index, word ->
                delay(ScriptConfiguration.READING_SPEED)
                newWord += word.toString()
                val newScriptLine =
                    ScriptBuilder(user, newWord, event, stage, id).addAnswer(newScript.answer)
                        .create()
                if (index == 0) {
                    scriptCache.add(newScriptLine)
                } else {
                    scriptCache.set(scriptCache.lastIndex, newScriptLine)
                }
                withContext(Dispatchers.Main) {
                    readingLineCallback(scriptCache)
                }
            }
        } else if(newScript.eventType == EventType.ANSWER){
            val answerScriptMessage = newScript.answer[selectedAnswerNumber] ?: ""
            answerScriptMessage.toCharArray().forEachIndexed { index, word ->
                delay(ScriptConfiguration.READING_SPEED)
                newWord += word.toString()
                val newScriptLine =
                    ScriptBuilder(user, newWord, 0, stage, id).addAnswer(newScript.answer).create()

                if(index == 0){
                    scriptCache.add(newScriptLine)
                }else{
                    scriptCache.set(scriptCache.lastIndex, newScriptLine)
                }

                withContext(Dispatchers.Main) {
                    readingLineCallback(scriptCache)
                }
            }
        }
        else if(newScript.eventType == EventType.CAMERA_RESULT){
            message.toCharArray().forEachIndexed { index, word ->
                delay(ScriptConfiguration.READING_SPEED)
                newWord += word.toString()
                val newScriptLine =
                    ScriptBuilder(user, newWord, event, stage, id).addAnswer(newScript.answer)
                        .create()
                if (index == 0) {
                    scriptCache.add(newScriptLine)
                } else {
                    scriptCache.set(scriptCache.lastIndex, newScriptLine)
                }
                withContext(Dispatchers.Main) {
                    readingLineCallback(scriptCache)
                }
            }
            //TODO : 사진을 보여주는 로직 추가하기
        }
        else if(newScript.eventType == EventType.DRAWING_RESULT){
            message.toCharArray().forEachIndexed { index, word ->
                delay(ScriptConfiguration.READING_SPEED)
                newWord += word.toString()
                val newScriptLine =
                    ScriptBuilder(user, newWord, event, stage, id).addAnswer(newScript.answer)
                        .create()
                if (index == 0) {
                    scriptCache.add(newScriptLine)
                } else {
                    scriptCache.set(scriptCache.lastIndex, newScriptLine)
                }
                withContext(Dispatchers.Main) {
                    readingLineCallback(scriptCache)
                }
            }
            //TODO : 사진을 보여주는 로직 추가하기
        }
        else {
            scriptCache.add(newScript)
            withContext(Dispatchers.Main) {
                readingLineCallback(scriptCache)
            }
        }
        delay(ScriptConfiguration.LINE_ENTER_SPEED)
        val lastScript = scriptCache.last().parse()
        completeReadingCallback(lastScript)

        return scriptCache
    }

    override suspend fun answerScriptLine(
        answerNumber: Int,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): MutableList<Script> {
        var newWord = ""
        val lastScript = scriptCache.last().parse()
        val (user, _, _, stage, id) = lastScript
        val answerScriptMessage = lastScript.answer[answerNumber] ?: ""
        answerScriptMessage.toCharArray().forEach { word ->
            delay(ScriptConfiguration.READING_SPEED)
            newWord += word.toString()
            val newScriptLine =
                ScriptBuilder(user, newWord, 0, stage, id).addAnswer(lastScript.answer).create()
            scriptCache.set(scriptCache.lastIndex, newScriptLine)
            withContext(Dispatchers.Main) {
                readingLineCallback(scriptCache)
            }
        }
        delay(ScriptConfiguration.LINE_ENTER_SPEED)
        val answerScript = scriptCache.last().parse()
        completeReadingCallback(answerScript)
        return scriptCache
    }

    override suspend fun takePictureScriptLine(
        isCompleted: Boolean,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script> {
        var newWord = ""
        val lastScript = scriptCache.last().parse()
        val (user, message, _, stage, id) = lastScript
        //TODO : 사진 찍었을 때, 로직은 여기에서 처리

        message.toCharArray().forEachIndexed { index, word ->
            delay(ScriptConfiguration.READING_SPEED)
            newWord += word.toString()
            val newScriptLine =
                ScriptBuilder(user, newWord, 4, stage, id).addAnswer(lastScript.answer).create()
            scriptCache.set(scriptCache.lastIndex, newScriptLine)
            withContext(Dispatchers.Main) {
                readingLineCallback(scriptCache)
            }
        }
        delay(ScriptConfiguration.LINE_ENTER_SPEED)
        val answerScript = scriptCache.last().parse()
        if(answerScript.event == 4){
            pageNumber++
        }
        completeReadingCallback(answerScript)
        return scriptCache
    }

    override suspend fun drawingImageScriptLine(
        isCompleted: Boolean,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): List<Script> {
        var newWord = ""
        val lastScript = scriptCache.last().parse()
        val (user, message, _, stage, id) = lastScript
//        TODO("그림 그렸을 때 여기로")

        message.toCharArray().forEachIndexed { index, word ->
            delay(ScriptConfiguration.READING_SPEED)
            newWord += word.toString()
            val newScriptLine =
                ScriptBuilder(user, newWord, if(isCompleted){
                    6
                }else{
                    5
                }, stage, id).addAnswer(lastScript.answer).create()
            scriptCache.set(scriptCache.lastIndex, newScriptLine)
            withContext(Dispatchers.Main) {
                readingLineCallback(scriptCache)
            }
        }
        delay(ScriptConfiguration.LINE_ENTER_SPEED)
        val answerScript = scriptCache.last().parse()
        if(answerScript.event == 6){
            pageNumber++
        }
        completeReadingCallback(answerScript)
        return scriptCache
    }

    override fun resetScript() {
        isStopScript = true
        selectedScriptNumber = -1
        scriptCache.clear()
    }

    override fun readNextScriptLine(
        scriptPageNumber: Int,
        completedStageCallback: () -> Unit
    ): Script? {
        val script = scriptStorage.search(scriptPageNumber)
        if (script.size <= pageNumber) {
            completedStageCallback()
            return null
        }
        val scriptLine = script[pageNumber]
        pageNumber++
        return scriptLine
    }

    override fun pauseScript() {
        this.isStopScript = false
    }

    override fun loadScript(scriptNumber: Int, loadedScripts: List<Script>): List<Script> {
        this.pageNumber = loadedScripts.size
        val allScripts = scriptStorage.search(scriptNumber)
        val mutableLoadedScripts = loadedScripts.toMutableList()
        mutableLoadedScripts.forEachIndexed { index, script ->
            if (script.event == 0) {
                scriptCache.add(script)
            } else {
                val script = ScriptBuilder(
                    script.user,
                    script.message,
                    script.event,
                    script.stage,
                    script.id
                ).addAnswer(allScripts[index].answer).create()
                scriptCache.add(script)
            }
        }
        return scriptCache.toList()
    }
}