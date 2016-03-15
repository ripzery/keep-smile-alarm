package com.EWIT.FrenchCafe.manager

import android.util.Log
import com.google.gson.Gson
import com.EWIT.FrenchCafe.extension.get
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.SharePref
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