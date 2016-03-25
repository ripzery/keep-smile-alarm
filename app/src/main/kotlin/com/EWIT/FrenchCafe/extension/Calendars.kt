package com.EWIT.FrenchCafe.extension

import com.EWIT.FrenchCafe.model.dao.Model
import java.util.*

/**
 * Created by Euro on 3/18/16 AD.
 */
//get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH) ||
//get(Calendar.MONTH) < b.get(Calendar.MONTH) ||
//get(Calendar.YEAR) < b.get(Calendar.YEAR) ||
//get(Calendar.HOUR_OF_DAY) * 60 * get(Calendar.MINUTE) < b.get(Calendar.HOUR_OF_DAY) * 60 * b.get(Calendar.MINUTE)
fun Calendar.minBefore(b: Calendar): Boolean {
    return timeInMillis / 60000 < b.timeInMillis / 60000
}

fun Calendar.toAlarmDao(): Model.AlarmDao {
    return Model.AlarmDao(Model.DatePicked(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)),
            Model.TimeWake(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE)), listOf())
}

fun Calendar.toAlarmDao(repeatDayList: List<Int>): Model.AlarmDao {
    return Model.AlarmDao(Model.DatePicked(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)),
            Model.TimeWake(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE)), repeatDayList)
}

fun Calendar.toAlarmDao(placePicked: Model.PlacePicked): Model.AlarmDao {
    return Model.AlarmDao(Model.DatePicked(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)),
            Model.TimeWake(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE)), listOf(), placePicked)
}

fun Calendar.toAlarmDao(repeatDayList: List<Int>, placePicked: Model.PlacePicked): Model.AlarmDao {
    return Model.AlarmDao(Model.DatePicked(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)),
            Model.TimeWake(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE)), repeatDayList, placePicked)
}

fun Calendar.toDatePicked(timeInMillis: Long = this.timeInMillis): Model.DatePicked {
    this.timeInMillis = timeInMillis
    return Model.DatePicked(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH))
}

fun Calendar.toTimeWake(timeInMillis: Long = this.timeInMillis): Model.TimeWake {
    this.timeInMillis = timeInMillis
    return Model.TimeWake(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
}

fun Calendar.getTimeFormat(): String {
    return String.format("%02d", get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", get(Calendar.MINUTE))
}