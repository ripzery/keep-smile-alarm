package com.EWIT.FrenchCafe.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.adapter.AlarmSetPagerAdapter
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.fragment.SmartAlarmFragment
import com.EWIT.FrenchCafe.fragment.ManualAlarmFragment
import com.EWIT.FrenchCafe.model.dao.Model
import kotlinx.android.synthetic.main.activity_alarm_set.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Euro on 3/10/16 AD.
 */

class AlarmSetActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var smartAlarmFragment: SmartAlarmFragment
    lateinit private var manualFragment: ManualAlarmFragment
    private var alarmDao : Model.AlarmDao? = null
    private var editIndex : Int = -1

    /** Static variable **/

    companion object{
        val EXTRA_ALARM_DAO = "ALARMDAO"
        val EXTRA_EDIT_INDEX = "EDIT_INDEX"
        val RESULT_CODE_ADD = 1
        val RESULT_CODE_EDIT = 2
    }

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
        initToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.extras != null) {
            alarmDao = intent.extras.getParcelable<Model.AlarmDao>(EXTRA_ALARM_DAO)
            editIndex = intent.extras.getInt(EXTRA_EDIT_INDEX)
        }

        if(alarmDao == null){ // set new alarm mod

            supportActionBar?.title = "Set Alarm"

        }else{ // edit mode

            supportActionBar?.title = "Edit Alarm"

        }

        var fm = supportFragmentManager
        val adapter = AlarmSetPagerAdapter(fm, this@AlarmSetActivity, alarmDao, editIndex)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        if(alarmDao?.placePicked != null){
            tabLayout.getTabAt(1)?.select()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    /** Listener zone **/

}