package com.EWIT.FrenchCafe.model.dao

import com.EWIT.FrenchCafe.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafe.dialog.TimePickerDialogFragment
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

object Model{

    @PaperParcel
    data class TimeWake(val hourOfDay: Int, val minute: Int): PaperParcelable {
        fun getTimeFormat() : String{
            return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    @PaperParcel
    data class DatePicked(val year: Int, val monthOfYear: Int, val dayOfMonth: Int): PaperParcelable {
        fun getDateFormat(): String{
            return String.format("%02d", dayOfMonth) +"/"+ String.format("%02d", monthOfYear) + "/" + year
        }
    }

    @PaperParcel
    data class PlacePicked(val arrivalPlace: String, val departurePlace: String, val arriveTime: Long): PaperParcelable

    @PaperParcel
    data class AlarmDao(var datePicked: DatePicked, var timeWake: TimeWake, var repeatDay: List<Int>, var placePicked: PlacePicked? = null): PaperParcelable

    @PaperParcel
    data class AlarmCollectionDao(var alarmCollectionList:ArrayList<AlarmDao>): PaperParcelable
}