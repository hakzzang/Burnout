package com.hbs.burnout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Script(
    @ColumnInfo var user: Int, @ColumnInfo var message: String,
    @ColumnInfo var event: Int, @ColumnInfo var stage: Int, @PrimaryKey var id: Int
) {
    @Ignore
    val answer = hashMapOf<Int, String>()

    @Ignore
    var userType: UserType = UserType.YOU

    @Ignore
    var eventType: EventType = EventType.CHATTING

    fun parse(): Script {
        userType = if (user == 0) {
            UserType.YOU
        }else{
            UserType.ME
        }
        eventType = when (event) {
            0 -> {
                EventType.CHATTING
            }
            1 -> {
                EventType.QUESTION
            }

            else -> {
                EventType.FREE
            }
        }
        return this
    }
}

class ScriptBuilder(user:Int, message:String, event:Int, stage:Int, id: Int){
    private val script = Script(user, message, event, stage, id)

    fun addAnswer(answer:Map<Int,String>): ScriptBuilder {
        script.answer.putAll(answer)
        return this
    }

    fun create(): Script {
        return script
    }
}

enum class UserType{
    YOU, ME
}

enum class EventType{
    CHATTING, QUESTION, ANSWER, CAMERA, FREE,
}
