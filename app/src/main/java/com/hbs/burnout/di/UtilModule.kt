package com.hbs.burnout.di

import android.content.Context
import androidx.room.Room
import com.hbs.burnout.model.dao.script.StageDataBase
import com.hbs.burnout.utils.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object UtilModule {
    @Provides
    fun provideStageDataBase(@ApplicationContext context: Context): StageDataBase =
        Room.databaseBuilder(context, StageDataBase::class.java, "STAGE_DATABASE.db").build()

    @Provides
    fun provideNotificationHelper() = NotificationHelper()
}