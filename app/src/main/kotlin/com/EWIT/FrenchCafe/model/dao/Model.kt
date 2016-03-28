package com.EWIT.FrenchCafe.model.dao

import com.EWIT.FrenchCafe.extension.mCalendar
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

object Model{

    @PaperParcel
    data class TimeWake(var hourOfDay: Int, val minute: Int): PaperParcelable {
        fun getTimeFormat() : String{
            return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    @PaperParcel
    data class DatePicked(val year: Int, val monthOfYear: Int, val dayOfMonth: Int): PaperParcelable {
        fun getDateFormat(): String{
            return String.format("%02d", dayOfMonth) +"/"+ String.format("%02d", monthOfYear+1) + "/" + year
        }
    }

    @PaperParcel
    data class PlacePicked(val arrivalPlace: PlaceDetail, val departurePlace: PlaceDetail, val arriveTime: Long, val travelTime: Long): PaperParcelable{
        fun getReadableTime(arriveTime: Long): String{
            val calendar = mCalendar()
            calendar.timeInMillis = arriveTime
            with(calendar){
                return String.format("%02d", get(Calendar.HOUR_OF_DAY)) +":" + String.format("%02d", get(Calendar.MINUTE))
            }
        }
    }

    @PaperParcel
    data class PlaceDetail(val id: String, var name: String, val latLng: PlaceLatLng): PaperParcelable

    @PaperParcel
    data class PlaceLatLng(val latitude: Double, val longitude: Double) : PaperParcelable

    @PaperParcel
    data class AlarmDao(var datePicked: DatePicked, var timeWake: TimeWake,
                        var repeatDay: List<Int>,
                        var placePicked: PlacePicked? = null,
                        var alarmSound: String? = null,
                        var isSwitchOn: Boolean = true): PaperParcelable{
        fun toCalendar(): Calendar{
            return GregorianCalendar(datePicked.year,
                    datePicked.monthOfYear,
                    datePicked.dayOfMonth,
                    timeWake.hourOfDay,
                    timeWake.minute)
        }

        fun isAlreadyWaked(): Boolean{
            return toCalendar().before(mCalendar())
        }
    }

    @PaperParcel
    data class AlarmCollectionDao(var alarmCollectionList:ArrayList<AlarmDao>): PaperParcelable {

        fun isHasSmartAlarm(): Boolean{
            for(alarm in alarmCollectionList){
                if(alarm.placePicked != null){
                    return true
                }
            }
            return false
        }

        fun sortAscending() {
            alarmCollectionList.sortWith(Comparator { alarm1, alarm2 ->
                alarm1.toCalendar().timeInMillis.toInt() - alarm2.toCalendar().timeInMillis.toInt()
            })
        }

        fun sortDecending() {
            alarmCollectionList.sortWith(Comparator { alarm1, alarm2 ->
                alarm2.toCalendar().timeInMillis.toInt() - alarm1.toCalendar().timeInMillis.toInt()
            })
        }

        fun getNewAlarmIndex(alarmList: ArrayList<AlarmDao>): Int{
            for(index in alarmCollectionList.indices){
                if(!alarmList.contains(alarmCollectionList[index])){
                    return index
                }
            }

            // No new alarm
            return -1
        }
    }
}