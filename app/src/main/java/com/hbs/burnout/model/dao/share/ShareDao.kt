package com.hbs.burnout.model.dao.share

import androidx.room.*
import com.hbs.burnout.model.ShareResult

@Dao
interface ShareDao {
    @Query("SELECT * FROM ShareResult WHERE round =:roundNum")
    suspend fun getShareDataBy(roundNum:Int): ShareResult

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(script: ShareResult): Long
}