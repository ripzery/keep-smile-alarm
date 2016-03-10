package com.socket9.eyealarm.model.dao

import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

object Model{
    data class AlarmDao(var datePicked: DatePickerDialogFragment.DatePicked, var timePicked: TimePickerDialogFragment.TimePicked)

    data class AlarmCollectionDao(var alarmCollectionDao:List<AlarmDao>)
}