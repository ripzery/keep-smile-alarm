package com.EWIT.FrenchCafe.interfaces

import com.EWIT.FrenchCafe.extension.save
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.CalendarAlarmConverter
import com.EWIT.FrenchCafe.util.SharePref
import com.EWIT.FrenchCafe.util.WaketimeUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_repeat_day.*
import kotlinx.android.synthetic.main.layout_time_picker.*

/**
 * Created by Euro on 3/13/16 AD.
 */


interface AlarmSetInterface {


    /** Override method zone **/

    fun onAlarmStarted(alarmDao: Model.AlarmDao)

    /** Method zone **/

    fun setAlarm(alarmDao: Model.AlarmDao) {

        /* check if the time is past or not, if pass set date to tomorrow */
        var correctedAlarmDao = shouldWakeTomorrow(alarmDao)

        /* update alarm collection in share preference */
        updateAlarmCollectionDao(correctedAlarmDao)

        /* start alarm */
        startAlarmReceiver(correctedAlarmDao)
    }


    /** Internal method zone **/

    private fun shouldWakeTomorrow(alarmDao: Model.AlarmDao) : Model.AlarmDao{
        val wakeTimeInMillis = WaketimeUtil.decideWakeupTimeMillis(alarmDao)
        alarmDao.datePicked = CalendarAlarmConverter.parseTimeInMillisToDatePicked(wakeTimeInMillis)
        return alarmDao
    }

    private fun startAlarmReceiver(alarmDao: Model.AlarmDao) {
        //        broadcast notification

        //        MyNotificationManager.broadcastNotificationIntent("Wakeup title",
        //                "Wakeup description",
        //                R.mipmap.icon,
        //                alarmDate.timeInMillis,
        //                alarmDao.hashCode().toLong())

        WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDao)

        onAlarmStarted(alarmDao)
    }

    private fun updateAlarmCollectionDao(alarmDao: Model.AlarmDao) {
        /* get alarmCollectionDao */
        var alarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()

        /* add alarm dao */
        alarmCollectionDao.alarmCollectionList.add(alarmDao)

        /* save back to share preference */
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
    }

}