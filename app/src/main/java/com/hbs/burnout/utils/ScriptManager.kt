package com.hbs.burnout.utils

import androidx.lifecycle.LiveDataScope
import com.hbs.burnout.model.Chatting
import kotlinx.coroutines.delay

object ScriptConfiguration {
    const val READING_SPEED = 250
}

interface ScriptManager {
    //scriptCallback은 Chatting 클래스에서 대본의 정보를 모두 담고 있고,
    //String을 통해서 speed에 따른 말하는 내용이 담겨져 있습니다.
    suspend fun playScript(missionNumber: Int, liveDataScope: LiveDataScope<List<Chatting>>)
}

class ScriptManagerImpl : ScriptManager {
    private val scriptStorage = ScriptStorage()

    override suspend fun playScript(
        missionNumber: Int,
        liveDataScope: LiveDataScope<List<Chatting>>
    ) {
        val script = searchScript(missionNumber)
        readScript(script, ScriptConfiguration.READING_SPEED, liveDataScope)
    }

    private fun searchScript(missionNumber: Int): List<Chatting> {
        return scriptStorage.search(missionNumber)
    }

    private suspend fun readScript(
        allScript: List<Chatting>,
        speed: Int,
        liveDataScope: LiveDataScope<List<Chatting>>
    ) {
        val newScript = mutableListOf<Chatting>()
        //0....10
        //안..녕..하..세..요
        //O(n2)
        allScript.forEachIndexed { index, script ->
            for (newWord in script.message.split("")) {
                delay(speed.toLong())
                updateScript(script, newScript, newWord, index)
                liveDataScope.emit(newScript)
            }
        }
    }

    private fun updateScript(script: Chatting, newScript:MutableList<Chatting>, newWord:String, index:Int){
        if(newScript.getOrNull(index) == null){
            newScript.add(index, Chatting(script.user, newWord, script.event, script.id))
        }else{
            val newMessage=  newScript[index].message + newWord
            newScript[index] = Chatting(script.user, newMessage , script.event, script.id)
        }
    }
}

class ScriptStorage {
    private val mission1 = listOf(
        Chatting(0, "안녕하세요", 0, 1),
        Chatting(0, "네~ 안녕하세요?", 0, 2),
        Chatting(0, "식사는 했어요", 0, 3),
        Chatting(0, "^ㅠ^ 난 밥먹었지롱", 0, 4),
        Chatting(0, "ㅡㅡ나갑니다", 0, 5)
    )

    fun search(missionNumber: Int): List<Chatting> {
        when (missionNumber) {
            1 -> return mission1
        }

        return mission1
    }
}
