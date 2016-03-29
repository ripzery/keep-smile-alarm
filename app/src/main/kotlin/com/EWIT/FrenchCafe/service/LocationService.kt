package com.EWIT.FrenchCafe.service

import android.app.IntentService
import android.content.Intent

/**
 * Created by Euro on 3/28/16 AD.
 */
class LocationService : IntentService {

    /** Variable zone **/


    /** Override method zone **/

    constructor(name: String): super(name){

    }

    constructor(): super("LocationService")

    override fun onHandleIntent(intent: Intent?) {

    }

}