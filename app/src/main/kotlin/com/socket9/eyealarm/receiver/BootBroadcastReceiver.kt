package com.socket9.eyealarm.receiver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast
import com.socket9.eyealarm.BaseApp
import com.socket9.eyealarm.manager.MyNotificationManager
import com.socket9.eyealarm.manager.WakeupAlarmManager
import com.socket9.eyealarm.service.NotificationService
import com.socket9.eyealarm.service.WakeupAlarmService

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

            //TODO: reset all alarm

        } else if(intent.type.equals(MyNotificationManager.SET_NOTIFICATION)){

            startNotificationService(intent, context)

        } else if(intent.type.equals(WakeupAlarmManager.WAKEUP_ALARM)){

            startSetWakeupAlarmService(intent, context)
        }
    }

    /** Method zone **/

    private fun startNotificationService(intent: Intent?, context: Context?){
        val notificationService = Intent(context, NotificationService::class.java)
        val bundle:Bundle = intent!!.extras
        notificationService.putExtras(bundle)
        notificationService.action = intent.action
        notificationService.type = intent.type

        context?.startService(notificationService)
    }

    private fun startSetWakeupAlarmService(intent: Intent?, context: Context?){
        val wakeupAlarmService = Intent(context, WakeupAlarmService::class.java)
//        val bundle:Bundle = intent!!.extras

        wakeupAlarmService.type = WakeupAlarmManager.WAKEUP_ALARM

        context?.startService(wakeupAlarmService)
    }

    private fun startCancelWakeupAlarmService(intent: Intent?, context: Context?){

    }

}