package com.EWIT.FrenchCafé

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import com.EWIT.FrenchCafé.extension.get
import com.EWIT.FrenchCafé.receiver.BootBroadcastReceiver
import com.EWIT.FrenchCafé.util.Contextor
import com.EWIT.FrenchCafé.util.SharePref

/**
 * Created by Euro on 3/10/16 AD.
 */
class BaseApp : Application(){

    override fun onCreate() {
        super.onCreate()

        SharePref.sharePref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        Contextor.context = this@BaseApp

        val isFirstTime : Boolean = get(SharePref.SHARE_PREF_KEY_IS_FIRST_TIME, false) as Boolean


        /* Set always enable broadcast receiver no matter reboot */
        if(isFirstTime){
           val receiver = ComponentName(this@BaseApp, BootBroadcastReceiver::class.java )
           val pm = this@BaseApp.packageManager

            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        }
    }
}