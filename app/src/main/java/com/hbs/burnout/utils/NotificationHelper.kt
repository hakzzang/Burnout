package com.hbs.burnout.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.util.Log
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

    fun showBubble(context: Context, stageNumber:Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val notificationManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val target = Intent(context, ChattingActivity::class.java).apply {
                putExtra(ActivityNavigation.STAGE_ROUND, stageNumber)
            }
            val bubbleIntent = PendingIntent.getActivity(context, 0, target, PendingIntent.FLAG_UPDATE_CURRENT /* flags */)
            val icon = Icon.createWithResource(context, R.mipmap.ic_launcher_round)

            val bubbleData = Notification.BubbleMetadata.Builder(bubbleIntent, icon)
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
                .setContentTitle("새로운 미션 진행")
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


    fun updateShortcuts(context: Context, stageNumber:Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val shortcutManager: ShortcutManager = context.getSystemService(ShortcutManager::class.java)
            val icon = Icon.createWithResource(context, R.mipmap.ic_launcher_round)
            val shortcut = ShortcutInfo.Builder(context, "ID$stageNumber")
                .setShortLabel("미션${stageNumber}으로 이동하기")
                .setLongLabel("미션${stageNumber}으로 이동하기")
                .setIcon(icon)
                .setIntent(Intent(context, ChattingActivity::class.java).setAction(Intent.ACTION_VIEW).putExtra(ActivityNavigation.STAGE_ROUND, stageNumber))
                .build()

            shortcutManager.dynamicShortcuts = listOf(shortcut)
        }
    }
}