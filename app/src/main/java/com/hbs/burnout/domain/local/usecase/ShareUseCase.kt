package com.hbs.burnout.domain.local.usecase

import android.util.Log
import com.hbs.burnout.domain.local.repository.ShareRepository
import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.model.ShareResult
import com.hbs.burnout.model.StageProgress
import javax.inject.Inject

interface ShareUseCase {
    suspend fun loadShareData(round: Int): ShareResult

    suspend fun saveShareData(shareResult: ShareResult): Long
}

class ShareUseCaseImpl @Inject constructor(
    private val stageRepository: StageRepository,
    private val shareRepository: ShareRepository
) : ShareUseCase {

    override suspend fun loadShareData(round: Int): ShareResult =
        shareRepository.getShareData(round)

    override suspend fun saveShareData(shareResult: ShareResult): Long {
        stageRepository.loadMission().mapIndexed {index, stage->
            if(stage.progress == StageProgress.PLAYING){
                shareResult.round = index + 1
            }
        }
        Log.d("shareResult-round", shareResult.round.toString())
        Log.d("shareResult-object", shareResult.toString())
        return shareRepository.insert(shareResult)
    }
}