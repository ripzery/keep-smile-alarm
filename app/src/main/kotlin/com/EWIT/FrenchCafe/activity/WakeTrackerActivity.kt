package com.EWIT.FrenchCafe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.fragment.WakeTrackerFragment
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model

/**
 * Created by Euro on 3/10/16 AD.
 */

class WakeTrackerActivity : AppCompatActivity(){
    
    /** Variable zone **/
    lateinit private var wakeTrackerFragment: WakeTrackerFragment

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFlagScreenOnEvenLocked()

        setContentView(R.layout.activity_wake_tracker)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        // Do nothing
        if(!wakeTrackerFragment.isCompleted) {
            toast("Open your eye for 15 sec to stop alarm")
        }else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    /** Method zone **/

    private fun setFlagScreenOnEvenLocked() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }


    private fun initInstance() {
        val alarmDao = intent.getParcelableExtra<Model.AlarmDao>(WakeupAlarmManager.INTENT_EXTRA_ALARM_DAO)
        wakeTrackerFragment = WakeTrackerFragment.getInstance(alarmDao.alarmSound)
        replaceFragment(R.id.contentContainer, wakeTrackerFragment)
    }


    /** Listener zone **/

}