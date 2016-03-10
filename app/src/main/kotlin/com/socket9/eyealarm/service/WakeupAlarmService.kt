package com.socket9.eyealarm.service

import android.app.IntentService
import android.content.Intent
import com.socket9.eyealarm.manager.WakeupAlarmManager

/**
 * Created by Euro on 3/10/16 AD.
 */
class WakeupAlarmService : IntentService {

    /** Variable zone **/



    /** Method zone **/

    constructor(name: String) : super(name){

    }

    constructor() : super("WakeupAlarmService"){

    }

    override fun onHandleIntent(intent: Intent?) {
        when(intent?.type){
            WakeupAlarmManager.WAKEUP_ALARM -> WakeupAlarmManager.createAlarm(this)
        }
    }

}