package com.EWIT.FrenchCafe.receiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.WakefulBroadcastReceiver
import com.EWIT.FrenchCafe.extension.mCalendar
import com.EWIT.FrenchCafe.extension.minBefore
import com.EWIT.FrenchCafe.manager.MyNotificationManager
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.service.NotificationService
import com.EWIT.FrenchCafe.service.WakeupAlarmService
import com.EWIT.FrenchCafe.util.WaketimeUtil

/**
 * Created by Euro on 3/10/16 AD.
 */
class BootBroadcastReceiver : WakefulBroadcastReceiver() {

    /** Variable zone **/

    /** Static method zone **/

    companion object {
        val ACTION_BOOT_COMPLETE: String = "android.intent.action.BOOT_COMPLETED"
    }

    /** Override zone **/

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(ACTION_BOOT_COMPLETE)) {
            /* reset alarm */
            val alarmCollectionDao: Model.AlarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()

            alarmCollectionDao.alarmCollectionList.forEach { alarmDao ->

                if (alarmDao.isSwitchOn) {
                    if (alarmDao.repeatDay.size > 0 || mCalendar().minBefore(alarmDao.toCalendar())) {
                        WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDao)
                    }
                }

            }

        } else if (intent.type.equals(MyNotificationManager.SET_NOTIFICATION)) {

            startNotificationService(intent, context)

        } else if (intent.type.equals(WakeupAlarmManager.WAKEUP_ALARM)) {
            var alarmDao: Model.AlarmDao = intent.getParcelableExtra<Model.AlarmDao>(WakeupAlarmManager.INTENT_EXTRA_ALARM_DAO)

            if (alarmDao.repeatDay.size > 0) {

                if (WaketimeUtil.isAlarmToday(alarmDao.repeatDay.toIntArray())) {

                    // if should alarm today then start service
                    startSetWakeupAlarmService(intent, context)
                }

            } else {
                startSetWakeupAlarmService(intent, context)
            }
        }
    }

    /** Method zone **/

    private fun startNotificationService(intent: Intent?, context: Context?) {
        val notificationService = Intent(context, NotificationService::class.java)
        val bundle: Bundle = intent!!.extras
        notificationService.putExtras(bundle)
        notificationService.action = intent.action
        notificationService.type = intent.type

        context?.startService(notificationService)
    }

    private fun startSetWakeupAlarmService(intent: Intent?, context: Context?) {
        val wakeupAlarmService = Intent(context, WakeupAlarmService::class.java)
        //        val bundle:Bundle = intent!!.extras

        wakeupAlarmService.type = WakeupAlarmManager.WAKEUP_ALARM
        wakeupAlarmService.action = intent?.action
        wakeupAlarmService.putExtra(WakeupAlarmManager.INTENT_EXTRA_ALARM_DAO, intent!!.getParcelableExtra<Model.AlarmDao>(WakeupAlarmManager.INTENT_EXTRA_ALARM_DAO))

        context?.startService(wakeupAlarmService)
    }

}