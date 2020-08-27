package com.hbs.burnout.domain.local.repository

import com.hbs.burnout.model.ShareResult
import com.hbs.burnout.model.dao.script.StageDataBase
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

interface ShareRepository {
    suspend fun getShareData(round: Int): ShareResult
    fun insert(shareResult: ShareResult): Long
}

@FragmentScoped
class ShareRepositoryImpl @Inject constructor(private val dataBase: StageDataBase) : ShareRepository {
    override suspend fun getShareData(round: Int): ShareResult =
        dataBase.getShareDao().getShareDataBy(round)

    override fun insert(shareResult: ShareResult): Long = dataBase.getShareDao().insert(shareResult)
}