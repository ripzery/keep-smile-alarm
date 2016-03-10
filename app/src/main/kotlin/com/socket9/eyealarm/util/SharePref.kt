package com.socket9.eyealarm.util

import android.content.SharedPreferences

/**
 * Created by Euro on 3/10/16 AD.
 */
object SharePref {
    var sharePref: SharedPreferences? = null

    fun putString(key:String, value:String){
        sharePref?.edit()?.putString(key,value)?.apply()
    }
}