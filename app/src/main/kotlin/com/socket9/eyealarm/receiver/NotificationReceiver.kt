package com.socket9.eyealarm.receiver

import android.app.IntentService
import android.content.Intent

/**
 * Created by Euro on 3/10/16 AD.
 */
class NotificationReceiver(name: String?) : IntentService(name) {

    /** Variable zone **/
    val SET_NOTIFICATION = "CREATE_NOTIFICATION"
    val CANCEL_NOTIFICATION = "CANCEL_NOTIFICATION"

    /** Override zone **/

    override fun onHandleIntent(intent: Intent?) {
        when(intent!!.type){
            SET_NOTIFICATION -> {
                createNotification()
            }
            CANCEL_NOTIFICATION -> {
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