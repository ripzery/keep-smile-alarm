package com.socket9.eyealarm.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.replaceFragment
import com.socket9.eyealarm.fragment.AlarmSetFragment

/**
 * Created by Euro on 3/10/16 AD.
 */

class AlarmSetActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var alarmSetFragment: AlarmSetFragment


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
        return super.onOptionsItemSelected(item)
    }

    /** Method zone **/

    private fun initInstance() {
        alarmSetFragment  = AlarmSetFragment.newInstance("alarmSetFragment")
        replaceFragment(R.id.contentContainer, alarmSetFragment)
    }

    /** Listener zone **/

}