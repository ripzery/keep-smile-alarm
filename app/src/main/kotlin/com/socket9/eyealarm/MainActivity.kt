package com.socket9.eyealarm

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socket9.eyealarm.extension.replaceFragment
import com.socket9.eyealarm.fragment.AlarmListFragment
import com.socket9.eyealarm.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {

    /** Variable zone **/
    lateinit private var mainFragment: MainFragment
    lateinit private var alarmListFragment: AlarmListFragment
    lateinit private var actionDrawerToggle: ActionBarDrawerToggle

    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(actionDrawerToggle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionDrawerToggle.onConfigurationChanged(newConfig)
    }

    /** Method zone **/

    private fun initInstance() {
        initToolbar()

        mainFragment = MainFragment.newInstance("mainFragment")
        alarmListFragment = AlarmListFragment.newInstance("alarmListFragment")

        replaceFragment(R.id.contentContainer, mainFragment)

        setupDrawerListener()
    }

    private fun switchFragmentDrawer(fragment: Fragment, item: MenuItem) {
        replaceFragment(R.id.contentContainer, fragment)
        with(item){
            this@MainActivity.title = title
        }

        drawerLayout.closeDrawers()
    }


    private fun initToolbar() {
        actionDrawerToggle = setupDrawerToggle()
        setSupportActionBar(toolbar)
        drawerLayout.addDrawerListener(actionDrawerToggle)
        navView.setCheckedItem(R.id.menu_main)
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    /** Listener zone **/

    private fun setupDrawerListener(){
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_main -> switchFragmentDrawer(mainFragment, it)
                R.id.menu_alarm_list -> switchFragmentDrawer(alarmListFragment, it)
            }
            true
        }
    }
}
