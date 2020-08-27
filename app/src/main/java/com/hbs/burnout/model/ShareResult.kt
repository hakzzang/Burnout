package com.hbs.burnout.model

import android.graphics.Bitmap

data class ShareResult(var title:String = "", var image:Bitmap? = null, var content:String="") {
    //  type, uri, title, content, List<Result>
    var eventType : EventType = EventType.CHATTING
    var resultList:List<Result> = mutableListOf()

    class Result(val title:String, val progress:Int)

}