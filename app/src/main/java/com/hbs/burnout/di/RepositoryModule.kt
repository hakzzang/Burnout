package com.hbs.burnout.di

import com.hbs.burnout.domain.local.repository.ScriptRepository
import com.hbs.burnout.domain.local.repository.ScriptRepositoryImpl
import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.domain.local.repository.StageRepositoryImpl
import com.hbs.burnout.model.dao.script.StageDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule{
    @Provides
    fun provideScriptRepository(dataBase: StageDataBase) : ScriptRepository = ScriptRepositoryImpl(dataBase)

    @Provides
    fun provideStageRepository(dataBase: StageDataBase) : StageRepository = StageRepositoryImpl(dataBase)
}