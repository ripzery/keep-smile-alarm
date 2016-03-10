package com.socket9.eyealarm

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Euro on 3/10/16 AD.
 */
class BaseApp : Application(){

    override fun onCreate() {
        super.onCreate()

        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

    }

    object SharePref {
        var sharePref: SharedPreferences? = null

        fun putString(key:String, value:String){
            sharePref?.edit()?.putString(key,value)?.apply()
        }
    }
}