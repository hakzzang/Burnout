package com.hbs.burnout.di

import com.hbs.burnout.domain.local.repository.ScriptRepository
import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.domain.local.usecase.ChattingUseCase
import com.hbs.burnout.domain.local.usecase.ChattingUseCaseImpl
import com.hbs.burnout.domain.local.usecase.MainUseCase
import com.hbs.burnout.domain.local.usecase.MainUseCaseImpl
import com.hbs.burnout.utils.script.ScriptManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideChattingUseCase(
        scriptRepository: ScriptRepository,
        stageRepository: StageRepository,
        scriptManager: ScriptManager
    ): ChattingUseCase = ChattingUseCaseImpl(scriptRepository, stageRepository, scriptManager)

    @Provides
    @Singleton
    fun provideMainUseCase(stageRepository: StageRepository): MainUseCase =
        MainUseCaseImpl(stageRepository)
}