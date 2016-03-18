package com.EWIT.FrenchCafe.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.EWIT.FrenchCafe.fragment.SmartAlarmFragment
import com.EWIT.FrenchCafe.fragment.ManualAlarmFragment
import com.EWIT.FrenchCafe.model.dao.Model

/**
 * Created by Euro on 3/16/16 AD.
 */

class AlarmSetPagerAdapter(var fm: FragmentManager, var context: Context, var alarmDao: Model.AlarmDao?) : FragmentPagerAdapter(fm) {

    private val PAGE_COUNT: Int = 2
    private val PAGE_TITLES = listOf("Manual Alarm", "Smart Alarm")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(index: Int): Fragment? {
        var showFragment: Fragment

        if (index == 0) {
            val manualAlarm = if(alarmDao?.placePicked == null) alarmDao else null
            showFragment = ManualAlarmFragment.newInstance(manualAlarm)
        } else {
            val smartAlarm = if(alarmDao?.placePicked != null) alarmDao else null
            showFragment = SmartAlarmFragment.newInstance(smartAlarm)
        }

        return showFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PAGE_TITLES[position]
    }

}