package com.socket9.eyealarm.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.replaceFragment
import com.socket9.eyealarm.fragment.WakeTrackerFragment

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
        wakeTrackerFragment = WakeTrackerFragment.getInstance()
        replaceFragment(R.id.contentContainer, wakeTrackerFragment)
        playAlarmSound()
    }

    private fun playAlarmSound(){
        //TODO: loop alarm sound

    }

    private fun cancelAlarmSound(){
        //TODO: remove alarm sound
    }

    /** Listener zone **/

}