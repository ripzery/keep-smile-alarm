package com.EWIT.FrenchCafe.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.EWIT.FrenchCafe.fragment.MapsAlarmFragment
import com.EWIT.FrenchCafe.fragment.WheelAlarmSetFragment

/**
 * Created by Euro on 3/16/16 AD.
 */

class AlarmSetPagerAdapter(var fm: FragmentManager, var context: Context) : FragmentPagerAdapter(fm) {

    private val PAGE_COUNT: Int = 2
    private val PAGE_TITLES = listOf("Manual Alarm", "Smart Alarm")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(index: Int): Fragment? {
        var showFragment: Fragment
        if (index == 0) {
            showFragment = WheelAlarmSetFragment.newInstance("WheelAlarmFragment")
        } else {
            showFragment = MapsAlarmFragment.newInstance("MapsAlarmFragment")
        }

        return showFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PAGE_TITLES[position]
    }

}