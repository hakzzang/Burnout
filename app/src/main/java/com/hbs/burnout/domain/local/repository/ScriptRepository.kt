package com.hbs.burnout.domain.local.repository

import com.hbs.burnout.model.Script
import com.hbs.burnout.model.dao.script.StageDataBase
import javax.inject.Inject

interface ScriptRepository {
    suspend fun loadScript(): List<Script>
    suspend fun loadScriptOf(scriptNumber:Int) : List<Script>
    fun insert(script: Script) : Long
}

class ScriptRepositoryImpl @Inject constructor(private val dataBase: StageDataBase): ScriptRepository {
    override suspend fun loadScript() = dataBase.getScriptDao().getAll()
    override suspend fun loadScriptOf(scriptNumber:Int): List<Script> = dataBase.getScriptDao().getScriptOf(scriptNumber)
    override fun insert(script: Script): Long = dataBase.getScriptDao().insert(script)
}