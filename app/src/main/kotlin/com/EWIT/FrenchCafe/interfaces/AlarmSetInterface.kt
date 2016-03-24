package com.EWIT.FrenchCafe.interfaces

import android.net.Uri
import android.util.Log
import com.EWIT.FrenchCafe.extension.*
import com.EWIT.FrenchCafe.fragment.SmartAlarmFragment
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.SharePref
import com.EWIT.FrenchCafe.util.WaketimeUtil
import com.google.gson.Gson
import java.util.*

/**
 * Created by Euro on 3/13/16 AD.
 */


interface AlarmSetInterface {

    /** Variable zone **/
    var alarmSoundUri: Uri?

    /** Override method zone **/

    fun onAlarmStarted(alarmDao: Model.AlarmDao)

    /** Method zone **/

    fun setAlarm(alarmDao: Model.AlarmDao) {

        /* update alarm sound uri */
        alarmDao.alarmSound = alarmSoundUri.toString()

        /* Set a correct time*/
        var correctedAlarmDao = modifyWakeupTime(alarmDao)

        /* update alarm collection in share preference */
        updateAlarmCollectionDao(correctedAlarmDao)

        /* start alarm */
        startAlarmReceiver(correctedAlarmDao)
    }

    fun updateAlarm(alarmDao: Model.AlarmDao, index: Int) {

        var alarmCollectionList = SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList

        /* cancel alarm */
        WakeupAlarmManager.cancelAlarm(WaketimeUtil.calculationWaketimeSummation(alarmCollectionList[index]))

        /* update alarm sound uri */
        alarmDao.alarmSound = alarmSoundUri.toString()

        /* Set a correct time*/
        var correctedAlarmDao = modifyWakeupTime(alarmDao)

        /* update new alarmDao */
        alarmCollectionList[index] = correctedAlarmDao

        /* save to sharePref */
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(Model.AlarmCollectionDao(alarmCollectionList)))

        startAlarmReceiver(correctedAlarmDao)

    }

    // TODO: 3 case
    /* Case 1 : currentTime < wakeupTime -> wakeup normally
    *  Case 2* : currentTime > wakeupTime && currentTime < wakeupTime + PERSONAL_TIME_OFFSET && currentTime + travelTime <= arriveTime -> wakeup immediately
    *  Case 3 : currentTime > arriveTime || currentTime + travelTime > arriveTime  -> wakeup tomorrow */

    companion object{
        fun modifyWakeupTime(originalAlarmDao: Model.AlarmDao): Model.AlarmDao {
            var shouldWakeupAlarmDao: Model.AlarmDao
            val wakeupTime = originalAlarmDao.toCalendar()
            val currentTime = mCalendar()

            if(originalAlarmDao.placePicked != null){
                val arriveTime = mCalendar()
                arriveTime.timeInMillis = originalAlarmDao.placePicked!!.arriveTime
                val travelTime = originalAlarmDao.placePicked!!.travelTime
                val currentTimePlusTravelTime = mCalendar()
                currentTimePlusTravelTime.timeInMillis = currentTime.timeInMillis + travelTime
                val wakeupPlusPersonalTime = mCalendar()
                wakeupPlusPersonalTime.timeInMillis = wakeupTime.timeInMillis + SmartAlarmFragment.PERSONAL_TIME_OFFSET

                if(currentTime.minBefore(wakeupTime)){
                    Log.d("Case 1", "Wake Normal")
                    shouldWakeupAlarmDao = originalAlarmDao
                }else if(wakeupTime.minBefore(currentTime) && currentTime.minBefore(currentTimePlusTravelTime) &&  currentTimePlusTravelTime.minBefore(arriveTime)){ // Case 2
                    Log.d("Case 2", "Wake Now")
                    val now = Calendar.getInstance()
                    val newDate = mCalendar().toDatePicked(now.timeInMillis)
                    val newTime = mCalendar().toTimeWake(now.timeInMillis)
                    shouldWakeupAlarmDao  = Model.AlarmDao(newDate, newTime, originalAlarmDao.repeatDay, originalAlarmDao.placePicked)
                } else {
                    Log.d("Case 3", "Wake Tomorrow")
                    shouldWakeupAlarmDao = shouldWakeTomorrow(originalAlarmDao)
                }

                return shouldWakeupAlarmDao

            }else{
                return shouldWakeTomorrow(originalAlarmDao)
            }

            //        Case 3
            //        if(arriveTime.minBefore(currentTime) || arriveTime.minBefore(currentTimePlusTravelTime)
            // Case 1

        }

        private fun shouldWakeTomorrow(alarmDao: Model.AlarmDao) : Model.AlarmDao{
            val wakeTimeInMillis = WaketimeUtil.decideWakeupTimeMillis(alarmDao)
            alarmDao.datePicked = mCalendar().toDatePicked(wakeTimeInMillis)
            return alarmDao
        }

    }


    /** Internal method zone **/

    private fun startAlarmReceiver(alarmDao: Model.AlarmDao) {
        //        broadcast notification

        //        MyNotificationManager.broadcastNotificationIntent("Wakeup title",
        //                "Wakeup description",
        //                R.mipmap.icon,
        //                alarmDate.timeInMillis,
        //                alarmDao.hashCode().toLong())


        Log.d("AlarmSetInterface", alarmDao.alarmSound)

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