package com.hbs.burnout.domain.local.usecase

import com.hbs.burnout.domain.local.repository.ShareRepository
import com.hbs.burnout.model.ShareResult
import javax.inject.Inject

interface ShareUseCase {
    suspend fun loadShareData(round: Int): ShareResult

    fun saveShareData(stage: ShareResult): Long
}

class ShareUseCaseImpl @Inject constructor(
    private val shareRepository: ShareRepository
) : ShareUseCase {

    override suspend fun loadShareData(round: Int): ShareResult =
        shareRepository.getShareData(round)

    override fun saveShareData(shareResult: ShareResult): Long = shareRepository.insert(shareResult)
}