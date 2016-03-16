package com.EWIT.FrenchCafe.interfaces

import com.EWIT.FrenchCafe.extension.save
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.SharePref
import com.google.gson.Gson

/**
 * Created by Euro on 3/13/16 AD.
 */


interface AlarmSetInterface {

    /** Override method zone **/

    fun onAlarmStarted(alarmDao: Model.AlarmDao)

    /** Method zone **/

    fun setAlarm(alarmDao: Model.AlarmDao) {
        /* update alarm collection in share preference */
        updateAlarmCollectionDao(alarmDao)

        /* start alarm */
        startAlarmReceiver(alarmDao)
    }

    /** Internal method zone **/

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