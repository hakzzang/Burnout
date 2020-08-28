package com.hbs.burnout.di

import com.hbs.burnout.domain.local.repository.ScriptRepository
import com.hbs.burnout.domain.local.repository.ShareRepository
import com.hbs.burnout.domain.local.repository.StageRepository
import com.hbs.burnout.domain.local.usecase.*
import com.hbs.burnout.utils.script.ScriptManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object UseCaseModule {
    @Provides
    fun provideChattingUseCase(
        scriptRepository: ScriptRepository,
        stageRepository: StageRepository,
        scriptManager: ScriptManager
    ): ChattingUseCase = ChattingUseCaseImpl(scriptRepository, stageRepository, scriptManager)

    @Provides
    fun provideMainUseCase(stageRepository: StageRepository): MainUseCase =
        MainUseCaseImpl(stageRepository)

    @Provides
    fun provideShareUseCase(shareRepository: ShareRepository): ShareUseCase =
        ShareUseCaseImpl(shareRepository)
}