package com.socket9.eyealarm

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.socket9.eyealarm.util.Contextor
import com.socket9.eyealarm.util.SharePref

/**
 * Created by Euro on 3/10/16 AD.
 */
class BaseApp : Application(){

    override fun onCreate() {
        super.onCreate()

        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        Contextor.context = this@BaseApp
    }
}