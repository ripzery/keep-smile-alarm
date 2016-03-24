package com.EWIT.FrenchCafe.manager

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.EWIT.FrenchCafe.activity.WakeTrackerActivity
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.receiver.BootBroadcastReceiver
import com.EWIT.FrenchCafe.util.Contextor
import com.EWIT.FrenchCafe.util.WaketimeUtil

/**
 * Created by Euro on 3/10/16 AD.
 */
object WakeupAlarmManager {
    /** Variable zone **/

    val WAKEUP_ALARM = "WAKEUP_ALARM"
    val CANCEL_ALARM = "CANCEL_ALARM"
    val INTENT_EXTRA_BROADCAST_REPEAT_DAY = "EXTRA://REPEAT_DAY"
    val INTENT_EXTRA_ALARM_SOUND = "EXTRA://ALARM_SOUND"

    /** Method zone**/

    /* wakeTimeSum is summation of wakeupTime including repeat alarm time */

    fun broadcastWakeupAlarmIntent(alarmDao: Model.AlarmDao) {
        /* get alarm manager */
        val alarmManager: AlarmManager = Contextor.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        /* create intent */
        val intent: Intent = Intent(Contextor.context, BootBroadcastReceiver::class.java)

        /* set type and action note: action must not be null */
        intent.action = "${WaketimeUtil.calculationWaketimeSummation(alarmDao)}"
        intent.putExtra(INTENT_EXTRA_ALARM_SOUND, alarmDao.alarmSound)
        intent.type = WAKEUP_ALARM

        /* if user also set repeat day */
        if (alarmDao.repeatDay.size != 0) {
            intent.putExtra(INTENT_EXTRA_BROADCAST_REPEAT_DAY, alarmDao.repeatDay.toIntArray())
        }

        /* set pending intent */
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(Contextor.context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        /* set wake time */
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDao.toCalendar().timeInMillis, alarmIntent)
    }

    fun startAlarm(wakeupAlarmService: IntentService, intent: Intent?) {
        /* start activity alarm */
        val wakeupIntent = Intent(wakeupAlarmService, WakeTrackerActivity::class.java)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        wakeupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        wakeupIntent.putExtra(INTENT_EXTRA_ALARM_SOUND, intent?.getStringExtra(INTENT_EXTRA_ALARM_SOUND))
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