package com.socket9.eyealarm.receiver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast
import com.socket9.eyealarm.BaseApp

/**
 * Created by Euro on 3/10/16 AD.
 */
class BootBroadcastReceiver : WakefulBroadcastReceiver(){

    companion object{
        val ACTION_BOOT_COMPLETE : String = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action.equals(ACTION_BOOT_COMPLETE)){
            /* reset alarm */
        }
    }

}