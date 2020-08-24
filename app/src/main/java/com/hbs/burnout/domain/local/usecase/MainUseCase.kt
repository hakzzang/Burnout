package com.hbs.burnout.domain.local.usecase

import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.model.Stage
import com.hbs.burnout.ui.main.adapter.MissionHelper
import javax.inject.Inject

interface MainUseCase {
    suspend fun loadMission(): List<Stage>
}

class MainUseCaseImpl @Inject constructor(private val stageRepository: StageRepository) : MainUseCase{
    override suspend fun loadMission() = MissionHelper.clearStageList(stageRepository.loadMission())
}