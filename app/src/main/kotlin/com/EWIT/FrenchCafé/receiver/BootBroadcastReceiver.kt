package com.EWIT.FrenchCafé.receiver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast
import com.EWIT.FrenchCafé.BaseApp
import com.EWIT.FrenchCafé.manager.MyNotificationManager
import com.EWIT.FrenchCafé.manager.WakeupAlarmManager
import com.EWIT.FrenchCafé.service.NotificationService
import com.EWIT.FrenchCafé.service.WakeupAlarmService
import com.EWIT.FrenchCafé.util.WaketimeUtil

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

            // TODO: check if this is the day which should be alarm

            var repeatDayList = intent.getIntegerArrayListExtra(WakeupAlarmManager.INTENT_BROADCAST_REPEAT_DAY)

            if(repeatDayList != null){
                // TODO: implement repeat day alarm in array list

            }else{
                // TODO : implement should alarm today or next day
                WaketimeUtil.decideWakeupTimeMillis()
            }

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

    private fun startSetWakeupAlarmService(intent: Intent?, context: Context?) {
        val wakeupAlarmService = Intent(context, WakeupAlarmService::class.java)
        //        val bundle:Bundle = intent!!.extras

        wakeupAlarmService.type = WakeupAlarmManager.WAKEUP_ALARM
        wakeupAlarmService.action = intent?.action

        context?.startService(wakeupAlarmService)
    }

}