package com.EWIT.FrenchCafe.util

import com.EWIT.FrenchCafe.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafe.dialog.TimePickerDialogFragment
import com.EWIT.FrenchCafe.model.dao.Model
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
object CalendarAlarmConverter {
    fun parseDateTimePicked(datePicked: Model.DatePicked, timeWake: Model.TimeWake) : GregorianCalendar {
        return GregorianCalendar(datePicked.year, datePicked.monthOfYear, datePicked.dayOfMonth, timeWake.hourOfDay, timeWake.minute)
    }

    fun parseAlarmDao(alarmDao: Model.AlarmDao) : GregorianCalendar {
        return GregorianCalendar(alarmDao.datePicked.year,
                alarmDao.datePicked.monthOfYear,
                alarmDao.datePicked.dayOfMonth,
                alarmDao.timeWake.hourOfDay,
                alarmDao.timeWake.minute)
    }

    fun parseTimeInMillisToDatePicked(timeInMillis: Long): Model.DatePicked {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis
        return Model.DatePicked(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun parseTimeInMillisToTimeWake(timeInMillis: Long): Model.TimeWake{
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis
        return Model.TimeWake(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

}