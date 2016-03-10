package com.socket9.eyealarm.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.socket9.eyealarm.receiver.BootBroadcastReceiver
import com.socket9.eyealarm.util.Contextor

/**
 * Created by Euro on 3/10/16 AD.
 */

object MyNotificationManager{

    /** Variable **/
    val SET_NOTIFICATION = "CREATE_NOTIFICATION"
    val CANCEL_NOTIFICATION = "CANCEL_NOTIFICATION"
    val EXTRA_TITLE = "title"
    val EXTRA_CONTENT = "content"
    val EXTRA_IC = "ic"
    val EXTRA_NOTI_ID = "notification_id"

    /** Method zone **/
    fun broadcastNotificationIntent(title:String, description: String, icon : Int, wakeTime: Long, notificationId: Long){
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)
        intent.type = SET_NOTIFICATION
        intent.action = "$notificationId"
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_CONTENT, description)
        intent.putExtra(EXTRA_IC, icon)
        intent.putExtra(EXTRA_NOTI_ID, notificationId)
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTime, alarmIntent)
    }
}