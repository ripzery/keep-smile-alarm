package com.socket9.eyealarm.util

import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import com.socket9.eyealarm.model.dao.Model
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
object CalendarConverter {
    fun parseDateTimePicked(datePicked: Model.DatePicked, timePicked: Model.TimePicked) : GregorianCalendar {
        return GregorianCalendar(datePicked.year, datePicked.monthOfYear, datePicked.dayOfMonth, timePicked.hourOfDay, timePicked.minute)
    }

    fun parseAlarmDao(alarmDao: Model.AlarmDao) : GregorianCalendar {
        return GregorianCalendar(alarmDao.datePicked.year,
                alarmDao.datePicked.monthOfYear,
                alarmDao.datePicked.dayOfMonth,
                alarmDao.timePicked.hourOfDay,
                alarmDao.timePicked.minute)
    }
}