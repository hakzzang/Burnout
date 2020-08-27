package com.hbs.burnout.model.dao.script

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.ShareResult
import com.hbs.burnout.model.dao.Converters
import com.hbs.burnout.model.dao.share.ShareDao

@Database(entities = [Script::class, Stage::class, ShareResult::class], version = 1)
@TypeConverters(Converters::class)
abstract class StageDataBase : RoomDatabase() {
    abstract fun getScriptDao(): ScriptDao
    abstract fun getStageDao(): StageDao
    abstract fun getShareDao(): ShareDao
}