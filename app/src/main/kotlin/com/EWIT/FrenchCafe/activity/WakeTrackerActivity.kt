package com.EWIT.FrenchCafe.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.*
import com.EWIT.FrenchCafe.fragment.WakeTrackerFragment
import com.EWIT.FrenchCafe.manager.CaptureManager
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.SharePref
import com.google.gson.Gson

/**
 * Created by Euro on 3/10/16 AD.
 */

class WakeTrackerActivity : AppCompatActivity(){
    
    /** Variable zone **/
    lateinit private var wakeTrackerFragment: WakeTrackerFragment
    private var intentData: Intent? = null

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView: View = window.decorView;
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
//        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        decorView.systemUiVisibility = uiOptions;
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setFlagScreenOnEvenLocked()
        setContentView(R.layout.activity_wake_tracker)
        initInstance()
        CaptureManager.fireScreenCaptureIntent(this@WakeTrackerActivity)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            toast("Record the screen...")
            intentData = CaptureManager.handleActivityResult(this@WakeTrackerActivity, requestCode, resultCode, data!!)
            wakeTrackerFragment.wakeWithRecording(intentData)
        }else{
            toast("Screen won't be recorded")
            wakeTrackerFragment.wakeWithoutRecording()
        }
        super.onActivityResult(requestCode, resultCode, data)
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

        if(alarmDao.repeatDay.size == 0) {
            val alarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()
            val index = alarmCollectionDao.alarmCollectionList.indexOf(alarmDao)
            alarmCollectionDao.alarmCollectionList[index].isSwitchOn = false
            save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
        }

        wakeTrackerFragment = WakeTrackerFragment.getInstance(alarmDao.alarmSound)
        replaceFragment(R.id.contentContainer, wakeTrackerFragment)
        supportsLowerLollipop {
            wakeTrackerFragment.wakeWithoutRecording()
        }
    }


    /** Listener zone **/

}