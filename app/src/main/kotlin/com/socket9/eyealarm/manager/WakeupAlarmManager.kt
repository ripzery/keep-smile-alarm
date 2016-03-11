package com.socket9.eyealarm.manager

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.socket9.eyealarm.activity.WakeTrackerActivity
import com.socket9.eyealarm.receiver.BootBroadcastReceiver
import com.socket9.eyealarm.util.Contextor

/**
 * Created by Euro on 3/10/16 AD.
 */
object WakeupAlarmManager {
    /** Variable zone **/

    val WAKEUP_ALARM = "WAKEUP_ALARM"


    /** Method zone**/

    fun broadcastWakeupAlarmIntent(wakeTime: Long){
        /* get alarm manager */
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        /* create intent */
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)

        /* set type and action note: action must not be null */

        intent.action = ""
        intent.type = WAKEUP_ALARM

        /* set pending intent */

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        /* set wake time */

        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTime, alarmIntent)
    }

    fun createAlarm(service: IntentService) {
        /* start activity alarm */
        val wakeupIntent = Intent(service, WakeTrackerActivity::class.java)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        service.startActivity(wakeupIntent)
    }

    fun cancelAlarm(){
        //TODO : implement cancel alarm
    }
}