package com.hbs.burnout.model.dao.script

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.Script

@Database(entities = [Script::class, Stage::class], version = 1)
abstract class StageDataBase : RoomDatabase() {
    abstract fun getScriptDao(): ScriptDao
    abstract fun getStageDao() : StageDao
}