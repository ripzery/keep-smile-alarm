package com.EWIT.FrenchCafe.extension

import android.os.Build
import android.util.Log
import com.EWIT.FrenchCafe.util.SharePref
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */

fun <T> Any.save(key: String, value: T){
    when(value){
        is Int -> SharePref.putInt(key, value)
        is String -> SharePref.putString(key, value)
        is Long -> SharePref.putLong(key, value)
        is Boolean -> SharePref.putBoolean(key, value)
        is Float -> SharePref.putFloat(key,value)
        else -> throw IllegalArgumentException("Can save only [Int, String, Long, Boolean, Float] !")
    }
}

fun <T> Any.get(id: String, defaultValue : T) : Any {
    var sp = SharePref.sharePref
    when(defaultValue){
        is Int -> return sp!!.getInt(id, defaultValue)
        is String -> return sp!!.getString(id, defaultValue)
        is Long -> return sp!!.getLong(id, defaultValue)
        is Boolean -> return sp!!.getBoolean(id, defaultValue)
        is Float -> return sp!!.getFloat(id, defaultValue)
        else -> throw IllegalArgumentException("Can get only [Int, String, Long, Boolean, Float] !")
    }
}

inline fun supportsLollipop(code: () -> Unit){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        code()
    }
}

inline fun supportsLowerLollipop(code: () -> Unit){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
       code()
    }
}

fun Any.currentMillis() : Long{
    return Calendar.getInstance().timeInMillis
}

fun Any.mCalendar() : Calendar{
    return Calendar.getInstance()
}