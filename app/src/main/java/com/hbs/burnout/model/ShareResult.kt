package com.hbs.burnout.model

import android.net.Uri

data class ShareResult(val title:String, val image:String, val content:String) {
    //  type, uri, title, content, List<Result>
    var eventType : EventType = EventType.CHATTING
    var resultList:List<Result> = mutableListOf()

    class Result(val title:String, val progress:Int)

}