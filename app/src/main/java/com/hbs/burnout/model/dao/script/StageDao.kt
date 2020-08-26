package com.hbs.burnout.model.dao.script

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hbs.burnout.model.Stage

@Dao
interface StageDao{
    @Query("SELECT * FROM Stage")
    suspend fun getAll(): List<Stage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stage: Stage) : Long
}
