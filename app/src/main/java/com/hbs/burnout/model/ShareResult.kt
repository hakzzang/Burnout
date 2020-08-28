package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
data class ShareResult constructor(
    @ColumnInfo @PrimaryKey var round: Int = 0,
    @ColumnInfo var title: String = "",
    @ColumnInfo var content: String = "",
    @ColumnInfo var uri: String? = "",
    @ColumnInfo var resultList: List<Result> = mutableListOf()) {

    @Ignore
    var eventType: EventType = EventType.CHATTING

    @Entity
    class Result(
        @ColumnInfo val title: String,
        @ColumnInfo val progress: Int
    )
}