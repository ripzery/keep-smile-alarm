package com.socket9.eyealarm.manager

import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
object GregorianCalendarConverterManager {
    fun parseDateTimePicked(datePicked: DatePickerDialogFragment.DatePicked, timePicked: TimePickerDialogFragment.TimePicked) : GregorianCalendar{
        return GregorianCalendar(datePicked.year, datePicked.monthOfYear, datePicked.dayOfMonth, timePicked.hourOfDay, timePicked.minute)
    }
}