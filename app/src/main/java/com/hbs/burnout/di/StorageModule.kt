package com.hbs.burnout.di

import com.hbs.burnout.utils.script.ScriptManager
import com.hbs.burnout.utils.script.ScriptManagerImpl
import com.hbs.burnout.utils.script.ScriptStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object StorageModule {
    @Provides
    fun provideScriptStorage() : ScriptStorage = ScriptStorage()

    @Provides
    fun provideScriptManager(scriptStorage: ScriptStorage) : ScriptManager =
        ScriptManagerImpl(scriptStorage)
}