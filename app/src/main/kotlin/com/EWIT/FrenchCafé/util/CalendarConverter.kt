package com.EWIT.FrenchCafé.util

import com.EWIT.FrenchCafé.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafé.dialog.TimePickerDialogFragment
import com.EWIT.FrenchCafé.model.dao.Model
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