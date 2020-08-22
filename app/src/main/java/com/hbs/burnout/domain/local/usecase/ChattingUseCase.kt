package com.hbs.burnout.domain.local.usecase

import com.hbs.burnout.domain.local.repository.ScriptRepository
import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.Stage
import com.hbs.burnout.utils.script.ScriptManager
import javax.inject.Inject

interface ChattingUseCase {
    suspend fun loadStage(): List<Stage>
    suspend fun loadScriptOf(scriptNumber: Int): List<Script>

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

    fun saveStage(stage: Stage): Long
    fun saveScript(script: Script): Long
    fun readNextScriptLine(scriptPageNumber: Int): Script

}

class ChattingUseCaseImpl @Inject constructor(
    private val scriptRepository: ScriptRepository,
    private val stageRepository: StageRepository,
    private val scriptManager: ScriptManager
) : ChattingUseCase {
    override suspend fun loadScriptOf(scriptNumber: Int) =
        scriptRepository.loadScriptOf(scriptNumber)

    override fun saveScript(script: Script): Long = scriptRepository.insert(script)

    override suspend fun loadStage() = stageRepository.loadMission()
    override fun saveStage(stage: Stage) = stageRepository.insert(stage)

    override fun readNextScriptLine(scriptPageNumber: Int) =
        scriptManager.readNextScriptLine(scriptPageNumber)

    override suspend fun readScriptLine(
        newScript: Script,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ) = scriptManager.readScriptLine(newScript, readingLineCallback, completeReadingCallback)


    override suspend fun answerScriptLine(
        answerNumber: Int,
        readingLineCallback: (List<Script>) -> Unit,
        completeReadingCallback: (Script) -> Unit
    ): MutableList<Script> = scriptManager.answerScriptLine(answerNumber, readingLineCallback, completeReadingCallback)
}