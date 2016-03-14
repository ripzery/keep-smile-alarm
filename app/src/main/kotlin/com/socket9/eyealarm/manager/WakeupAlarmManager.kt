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
    /** Variable zone **/

    val WAKEUP_ALARM = "WAKEUP_ALARM"
    val CANCEL_ALARM = "CANCEL_ALARM"

    /** Method zone**/

    /* wakeTimeSum is summation of wakeupTime including repeat alarm time */

    fun broadcastWakeupAlarmIntent(wakeTimeSum: Long) {
        /* get alarm manager */
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        /* create intent */
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)

        /* set type and action note: action must not be null */

        intent.action = "$wakeTimeSum"
        intent.type = WAKEUP_ALARM

        /* set pending intent */

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        /* set wake time */

        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeTimeSum, alarmIntent)
    }

    fun createAlarm(wakeupAlarmService: IntentService) {
        /* start activity alarm */

        val wakeupIntent = Intent(wakeupAlarmService, WakeTrackerActivity::class.java)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        wakeupAlarmService.startActivity(wakeupIntent)
    }

    /* wakeTimeSum is summation of wakeupTime including repeat alarm time */
    fun cancelAlarm(wakeTimeSum: Long) {
        val intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)
        intent.type = WAKEUP_ALARM
        intent.action = "$wakeTimeSum"
        val alarmIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = Contextor.context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }
}