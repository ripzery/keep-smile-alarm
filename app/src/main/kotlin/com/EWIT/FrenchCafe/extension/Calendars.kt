package com.EWIT.FrenchCafe.extension

import java.util.*

/**
 * Created by Euro on 3/18/16 AD.
 */

fun Calendar.minBefore(b: Calendar) : Boolean {
    if (get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH) ||
            get(Calendar.MONTH) < b.get(Calendar.MONTH) ||
            get(Calendar.YEAR) < b.get(Calendar.YEAR) ||
            get(Calendar.HOUR_OF_DAY) < b.get(Calendar.HOUR_OF_DAY) ||
            get(Calendar.MINUTE) < b.get(Calendar.MINUTE)) {

        return true
    }

    return false
}

fun Calendar.getTimeFormat() : String{
    return String.format("%02d", get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", get(Calendar.MINUTE))
}