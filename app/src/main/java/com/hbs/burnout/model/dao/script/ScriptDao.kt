package com.hbs.burnout.model.dao.script

import androidx.room.*
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.UserType
import java.util.*

@Dao
interface ScriptDao {
    @Query("SELECT * FROM Script")
    suspend fun getAll(): List<Script>

    @Query("SELECT * FROM Script WHERE stage = :stageNumber")
    suspend fun getScriptOf(stageNumber: Int): List<Script>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(script: Script): Long

    @Query("DELETE FROM Script")
    fun dropTable()
}