package com.EWIT.FrenchCafe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.fragment.AlarmSetFragment
import com.EWIT.FrenchCafe.fragment.WheelAlarmSetFragment

/**
 * Created by Euro on 3/10/16 AD.
 */

class AlarmSetActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var alarmSetFragment: AlarmSetFragment
    lateinit private var wheelFragment: WheelAlarmSetFragment


    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_set)
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
        when(item?.itemId){
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }


    /** Method zone **/

    private fun initInstance() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Set Alarm"

        alarmSetFragment  = AlarmSetFragment.newInstance("alarmSetFragment")
        wheelFragment  = WheelAlarmSetFragment.newInstance("wheelDateTimeFragment")
        replaceFragment(R.id.contentContainer, wheelFragment)
    }

    /** Listener zone **/

}