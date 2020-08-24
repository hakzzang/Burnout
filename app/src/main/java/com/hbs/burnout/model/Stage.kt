package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Stage(
    @ColumnInfo @PrimaryKey(autoGenerate = true) val round: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val thumbnail:String = "",
    @ColumnInfo var progress:Int
)