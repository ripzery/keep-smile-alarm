package com.EWIT.FrenchCafe.manager

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.EWIT.FrenchCafe.MainActivity
import com.EWIT.FrenchCafe.receiver.BootBroadcastReceiver
import com.EWIT.FrenchCafe.util.Contextor

/**
 * Created by Euro on 3/10/16 AD.
 */

object MyNotificationManager {

    /** Variable **/

    val SET_NOTIFICATION = "CREATE_NOTIFICATION"
    val CANCEL_NOTIFICATION = "CANCEL_NOTIFICATION"
    val SET_ALARM = "SET_ALARM"
    val EXTRA_TITLE = "title"
    val EXTRA_CONTENT = "content"
    val EXTRA_IC = "ic"
    val EXTRA_NOTI_ID = "notification_id"

    /** Method zone **/

    fun broadcastNotificationIntent(title: String, description: String, icon: Int, wakeTime: Long, notificationId: Long) {
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)
        intent.type = SET_NOTIFICATION
        intent.action = "$notificationId"
        val bundle:Bundle = Bundle()
        bundle.putString(EXTRA_TITLE, title)
        bundle.putString(EXTRA_CONTENT, description)
        bundle.putInt(EXTRA_IC, icon)
        bundle.putLong(EXTRA_NOTI_ID, notificationId)
        intent.action = ""
        intent.putExtras(bundle)
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTime, alarmIntent)
    }

    fun createNotification(title: String, msg: String, icon: Int, notificationId: Long) {
        val clickNotificationIntent: Intent = Intent(Contextor.context, MainActivity::class.java)

        val resultPendingIntent: PendingIntent = PendingIntent.getActivity(Contextor.context, 0,
                clickNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT)

        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(Contextor.context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(100))
                .setContentText(msg)

        val notificationManager: NotificationManager = Contextor.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId.toInt(), mBuilder.build())

    }
}