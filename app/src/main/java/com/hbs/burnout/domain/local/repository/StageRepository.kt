package com.hbs.burnout.domain.local.repository

import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.dao.script.StageDataBase
import javax.inject.Inject

interface StageRepository {
    suspend fun loadMission(): List<Stage>
    fun insert(stage: Stage): Long
}

class StageRepositoryImpl @Inject constructor(private val dataBase: StageDataBase) : StageRepository {
    override suspend fun loadMission() = dataBase.getStageDao().getAll()
    override fun insert(stage: Stage): Long = dataBase.getStageDao().insert(stage)
}