package com.socket9.eyealarm

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.socket9.eyealarm.util.Contextor
import com.socket9.eyealarm.util.SharePref

/**
 * Created by Euro on 3/10/16 AD.
 */
class BaseApp : Application(){

    override fun onCreate() {
        super.onCreate()

        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        if(SharePref.sharePref != null) {
            Log.d("SharePref ", " is not null")
        }

        Contextor.context = this@BaseApp
    }
}