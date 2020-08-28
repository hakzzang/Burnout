package com.hbs.burnout.di

import com.hbs.burnout.domain.local.repository.*
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

    @Provides
    fun provideShareRepository(dataBase: StageDataBase) : ShareRepository = ShareRepositoryImpl(dataBase)
}