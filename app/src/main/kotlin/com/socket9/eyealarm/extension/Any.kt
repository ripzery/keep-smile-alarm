package com.socket9.eyealarm.extension

import android.util.Log
import com.socket9.eyealarm.util.SharePref

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