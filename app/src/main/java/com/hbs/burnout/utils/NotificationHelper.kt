package com.hbs.burnout.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.core.content.getSystemService
import com.hbs.burnout.R
import com.hbs.burnout.ui.chat.ChattingActivity
import java.util.*

object NotificationConfiguration {
    const val NOTIFICATION_ID = 1001
    const val REQUEST_BUBBLE = 2001
    const val CHANNEL_ID = "CHANNEL_3001"
    const val CHANNEL_MESSAGE = "NEW_MISSION"

}

class NotificationHelper {
    fun makeNotificationChannel(context: Context){
        val notificationManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NotificationConfiguration.CHANNEL_ID,
                context.getString(R.string.text_new_message_mission),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.text_new_message_mission)
            }
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    fun showBubble(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val notificationManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val target = Intent(context, ChattingActivity::class.java)
            val bubbleIntent = PendingIntent.getActivity(context, 0, target, 0 /* flags */)
            val icon = Icon.createWithResource(context, R.mipmap.ic_launcher_round)

            val bubbleData = Notification.BubbleMetadata.Builder(
                PendingIntent.getActivity(
                    context,
                    NotificationConfiguration.REQUEST_BUBBLE,
                    // Launch BubbleActivity as the expanded bubble.
                    Intent(context, ChattingActivity::class.java)
                        .setAction(Intent.ACTION_VIEW),
                    PendingIntent.FLAG_UPDATE_CURRENT
                ), icon
            )
                .setDesiredHeight(600)
                .setAutoExpandBubble(false)
                .setSuppressNotification(true)
                .setIntent(bubbleIntent)
                .build()

            // Create notification
            val person: Person = Person.Builder()
                .setBot(true)
                .setName("Burnout")
                .setImportant(true)
                .build()

            val notification = Notification.Builder(context, NotificationConfiguration.CHANNEL_ID)
                .setContentIntent(bubbleIntent)
                .setContentTitle("미션")
                .setContentText("버블 알림을 통해서 미션을 확인해보세요")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setBubbleMetadata(bubbleData)
                .addPerson(person)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setShowWhen(true)
                .setStyle(Notification.MessagingStyle(person))
                .setWhen(Date().time).build()

            notificationManager?.notify(NotificationConfiguration.NOTIFICATION_ID, notification)
        }

    }
}