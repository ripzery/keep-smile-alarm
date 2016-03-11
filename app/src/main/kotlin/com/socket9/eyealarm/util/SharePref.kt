package com.socket9.eyealarm.util

import android.content.SharedPreferences

/**
 * Created by Euro on 3/10/16 AD.
 */
object SharePref {
    var sharePref: SharedPreferences? = null

    val SHARE_PREF_KEY_ALARM_COLLECTION_JSON = "ALARM_COLLECTION_JSON"

    fun putString(key:String, value:String){
        sharePref?.edit()?.putString(key,value)?.apply()
    }

    fun putInt(key:String, value:Int){
        sharePref?.edit()?.putInt(key,value)?.apply()
    }

    fun putLong(key:String, value:Long){
        sharePref?.edit()?.putLong(key,value)?.apply()
    }

    fun putFloat(key:String, value:Float){
        sharePref?.edit()?.putFloat(key, value)?.apply()
    }

    fun putBoolean(key:String, value:Boolean){
        sharePref?.edit()?.putBoolean(key, value)?.apply()
    }

}