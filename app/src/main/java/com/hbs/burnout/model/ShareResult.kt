package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class ShareResult(
    @ColumnInfo @PrimaryKey var round: Int,
    @ColumnInfo val title: String = "",
    @ColumnInfo val content: String,
    @ColumnInfo val uri: String? = "",
    @ColumnInfo var resultList: List<Result> = mutableListOf()) {

    @Ignore
    var eventType: EventType = EventType.CHATTING

    @Entity
    class Result(
        @ColumnInfo val title: String,
        @ColumnInfo val progress: Int
    )
}