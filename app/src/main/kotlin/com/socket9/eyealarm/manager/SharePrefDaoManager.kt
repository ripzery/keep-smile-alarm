package com.socket9.eyealarm.manager

import android.util.Log
import com.google.gson.Gson
import com.socket9.eyealarm.extension.get
import com.socket9.eyealarm.model.dao.Model
import com.socket9.eyealarm.util.SharePref
import java.util.*

/**
 * Created by Euro on 3/11/16 AD.
 */

object SharePrefDaoManager{

    fun getAlarmCollectionDao() : Model.AlarmCollectionDao{
        val alarmCollectionJson: String = get(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, "") as String
        var alarmCollectionDao: Model.AlarmCollectionDao?

        if (!alarmCollectionJson.isNullOrBlank()) {
            /* alarm was exist */

            alarmCollectionDao = Gson().fromJson(alarmCollectionJson, Model.AlarmCollectionDao::class.java)

        } else {
            /* blank alarm collection */
            alarmCollectionDao = Model.AlarmCollectionDao(ArrayList<Model.AlarmDao>())

        }

        return alarmCollectionDao!!
    }
}