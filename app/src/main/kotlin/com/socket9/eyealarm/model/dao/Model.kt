package com.socket9.eyealarm.model.dao

import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

object Model{
    data class TimePicked(val hourOfDay: Int, val minute: Int){
        fun getTimeFormat() : String{
            return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    data class DatePicked(val year: Int, val monthOfYear: Int, val dayOfMonth: Int){
        fun getDateFormat(): String{
            return String.format("%02d", dayOfMonth) +"/"+ String.format("%02d", monthOfYear) + "/" + year
        }
    }

    data class AlarmDao(var datePicked: DatePicked, var timePicked: TimePicked, var repeatDay: ArrayList<Int>)

    data class AlarmCollectionDao(var alarmCollectionList:ArrayList<AlarmDao>)
}