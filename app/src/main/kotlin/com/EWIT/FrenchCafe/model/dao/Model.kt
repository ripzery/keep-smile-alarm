package com.EWIT.FrenchCafe.model.dao

import com.EWIT.FrenchCafe.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafe.dialog.TimePickerDialogFragment
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

object Model{
    data class TimeWake(val hourOfDay: Int, val minute: Int){
        fun getTimeFormat() : String{
            return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    data class DatePicked(val year: Int, val monthOfYear: Int, val dayOfMonth: Int){
        fun getDateFormat(): String{
            return String.format("%02d", dayOfMonth) +"/"+ String.format("%02d", monthOfYear) + "/" + year
        }
    }

    data class PlacePicked(val arrivalPlace: String, val departurePlace: String, val arriveTime: Long)

    data class AlarmDao(var datePicked: DatePicked, var timeWake: TimeWake, var repeatDay: List<Int>, var placePicked: PlacePicked? = null)

    data class AlarmCollectionDao(var alarmCollectionList:ArrayList<AlarmDao>)
}