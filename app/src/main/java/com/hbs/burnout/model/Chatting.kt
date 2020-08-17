package com.hbs.burnout.model

data class Chatting(val user:Int, val message:String, val event:Int){
    var userType : UserType = UserType.YOU
    var eventType : EventType = EventType.CHATTING
    fun parse(){
        userType = if(user == 0){
            UserType.YOU
        }else{
            UserType.ME
        }
        eventType = when (event) {
            0 -> {
                EventType.CHATTING
            }
            1 -> {
                EventType.CHATTING
            }
            else -> {
                EventType.FREE
            }
        }
    }
}

enum class UserType{
    YOU, ME
}

enum class EventType{
    CHATTING, CAMERA, FREE, SELECTION
}
