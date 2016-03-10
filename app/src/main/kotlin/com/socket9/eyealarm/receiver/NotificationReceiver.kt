package com.socket9.eyealarm.receiver

import android.app.IntentService
import android.content.Intent
import com.socket9.eyealarm.manager.MyNotificationManager

/**
 * Created by Euro on 3/10/16 AD.
 */
class NotificationReceiver(name: String?) : IntentService(name) {

    /** Variable zone **/


    /** Override zone **/

    override fun onHandleIntent(intent: Intent?) {
        when(intent!!.type){
            MyNotificationManager.SET_NOTIFICATION -> {
                createNotification()
            }
            MyNotificationManager.CANCEL_NOTIFICATION -> {
                cancelNotification()
            }
            else -> {

            }
        }
    }

    /** Method zone **/

    private fun createNotification(){

    }

    private fun cancelNotification(){

    }
}