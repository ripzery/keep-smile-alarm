package com.socket9.eyealarm.receiver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast
import com.socket9.eyealarm.BaseApp
import com.socket9.eyealarm.manager.MyNotificationManager
import com.socket9.eyealarm.manager.WakeupAlarmManager

/**
 * Created by Euro on 3/10/16 AD.
 */
class BootBroadcastReceiver : WakefulBroadcastReceiver(){

    /** Variable zone **/

    /** Static method zone **/

    companion object{
        val ACTION_BOOT_COMPLETE : String = "android.intent.action.BOOT_COMPLETED"
    }

    /** Override zone **/

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action.equals(ACTION_BOOT_COMPLETE)){
            /* reset alarm */

        } else if(intent.type.equals(MyNotificationManager.SET_NOTIFICATION)){

            MyNotificationManager.createNotification(intent.getStringExtra(MyNotificationManager.EXTRA_TITLE),
                    intent.getStringExtra(MyNotificationManager.EXTRA_CONTENT),
                    intent.getIntExtra(MyNotificationManager.EXTRA_IC, 0),
                    intent.getLongExtra(MyNotificationManager.EXTRA_NOTI_ID, 0))


        } else if(intent.type.equals(WakeupAlarmManager.WAKEUP_ALARM)){

            WakeupAlarmManager.createAlarm(intent)
        }
    }

    /** Method zone **/

}