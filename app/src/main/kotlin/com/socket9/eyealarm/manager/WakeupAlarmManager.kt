package com.socket9.eyealarm.manager

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.socket9.eyealarm.activity.WakeTrackerActivity
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
        intent.action = ""
        intent.type = WAKEUP_ALARM
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTime, alarmIntent)
    }

    fun createAlarm(service: IntentService) {
        /* start activity alarm */
        val wakeupIntent = Intent(service, WakeTrackerActivity::class.java)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        service.startActivity(wakeupIntent)

        //TODO: play alarm sound

    }

    fun cancelAlarm(){
        //TODO : implement cancel alarm
    }
}