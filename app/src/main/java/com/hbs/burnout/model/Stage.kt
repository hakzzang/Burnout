package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hbs.burnout.utils.script.MissionHelper

@Entity
data class Stage(
    @ColumnInfo @PrimaryKey(autoGenerate = true) val round: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val thumbnail:String = "",
    @ColumnInfo var progress:Int
){
    //TODO : isCompleted 로직에는 오직 progress == COMPLETED 일 때만으로 해야됨
    //TODO : 지금은 PLAYING 중일 때도 맞다고 함
    fun isCompleted() : Boolean{
        return progress != StageProgress.NOT_COMPLETED
    }
}

class StageProgress{
    companion object{
        const val NOT_COMPLETED = 0
        const val PLAYING = 1
        const val COMPLETED = 2
    }
}

