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
                createNotification(intent)
            }
            MyNotificationManager.CANCEL_NOTIFICATION -> {
                cancelNotification()
            }
            else -> {

            }
        }
    }

    /** Method zone **/

    private fun createNotification(intent: Intent?) {
        val title = intent!!.getStringExtra(MyNotificationManager.EXTRA_TITLE)
        val content = intent.getStringExtra(MyNotificationManager.EXTRA_CONTENT)
        val ic = intent.getIntExtra(MyNotificationManager.EXTRA_IC, 0)
        val notificationId = intent.getLongExtra(MyNotificationManager.EXTRA_NOTI_ID, 0)

        MyNotificationManager.createNotification(title, content, ic, notificationId)
    }

    private fun cancelNotification(){

    }
}