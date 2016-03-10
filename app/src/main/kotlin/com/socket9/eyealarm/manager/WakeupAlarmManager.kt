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
object WakeupAlarmManager {
    /** Variable **/

    val WAKEUP_ALARM = "WAKEUP_ALARM"


    /** Method **/

    fun broadcastWakeupAlarmIntent(wakeTime: Long){
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTime, alarmIntent)
    }

    fun createAlarm(intent: Intent?) {
        //TODO: implement create alarm

    }

    fun cancelAlarm(){

    }
}