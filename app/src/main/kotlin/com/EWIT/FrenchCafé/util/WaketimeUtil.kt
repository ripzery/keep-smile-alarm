package com.EWIT.FrenchCafé.util

import com.EWIT.FrenchCafé.model.dao.Model
import java.util.*

/**
 * Created by Euro on 3/14/16 AD.
 */

object WaketimeUtil {

    /* calculate a summation of waketime in millisec of all repeat alarms
    * to be use as an uniquely alarm id which will guarantee that
    * won't be overlap each other */

    fun calculationWaketimeSummation(alarmDao: Model.AlarmDao): Long {
        var summationWaketime: Long = 0

        if (alarmDao.repeatDay.size > 0) {

            /* if user set repeat day */

            for (day in alarmDao.repeatDay) {
                var calendar = Calendar.getInstance()

                calendar.set(Calendar.DAY_OF_WEEK, day)

                with(alarmDao.timePicked) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                }

                summationWaketime += calendar.timeInMillis
            }

        } else {
            alarmDao.datePicked.dayOfMonth
            summationWaketime = decideWakeupTimeMillis(alarmDao)
        }

        return summationWaketime
    }

    /* decide if time is past, alarm will set tomorrow else today  */
    fun decideWakeupTimeMillis(alarmDao: Model.AlarmDao): Long {
        val todayNow = Calendar.getInstance()
        return if (CalendarConverter.parseAlarmDao(alarmDao).before(todayNow)) getNextDayTimeInMillis(alarmDao) else CalendarConverter.parseAlarmDao(alarmDao).timeInMillis
    }

    private fun getNextDayTimeInMillis(alarmDao: Model.AlarmDao): Long {
        return CalendarConverter.parseAlarmDao(alarmDao).timeInMillis + 24 * 60 * 60 * 1000
    }
}