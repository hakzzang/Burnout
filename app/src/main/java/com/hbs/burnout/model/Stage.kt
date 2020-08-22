package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stage(
    @ColumnInfo
    val stage: Int,
    @ColumnInfo
    val lastMessageNumber: Int,
    @ColumnInfo
    val isCompleted: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0
)