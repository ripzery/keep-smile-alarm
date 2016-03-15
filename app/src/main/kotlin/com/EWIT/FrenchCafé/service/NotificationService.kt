package com.EWIT.FrenchCafé.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.WakefulBroadcastReceiver
import com.EWIT.FrenchCafé.manager.MyNotificationManager

/**
 * Created by Euro on 3/10/16 AD.
 */
class NotificationService : IntentService{

    /** Variable zone **/


    /** Override zone **/

    constructor(name: String) : super(name) {

    }

    constructor() : super("NotificationService")

    override fun onHandleIntent(intent: Intent?) {
        when(intent!!.type){
            MyNotificationManager.SET_NOTIFICATION -> {
                createNotification(intent)
            }
            MyNotificationManager.CANCEL_NOTIFICATION -> {
                cancelNotification()
            }
            else -> {

            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent)
    }

    /** Method zone **/

    private fun createNotification(intent: Intent?) {
        val bundle: Bundle = intent!!.extras

        val title = bundle.getString(MyNotificationManager.EXTRA_TITLE)
        val content = bundle.getString(MyNotificationManager.EXTRA_CONTENT)
        val ic = bundle.getInt(MyNotificationManager.EXTRA_IC, 0)
        val notificationId = bundle.getLong(MyNotificationManager.EXTRA_NOTI_ID, 0)

        MyNotificationManager.createNotification(title, content, ic, notificationId)
    }

    private fun cancelNotification(){

    }
}