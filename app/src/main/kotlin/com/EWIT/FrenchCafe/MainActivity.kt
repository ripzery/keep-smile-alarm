package com.EWIT.FrenchCafe

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.EWIT.FrenchCafe.activity.AlarmSetActivity
import com.EWIT.FrenchCafe.extension.addFragment
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.fragment.AlarmListFragment
import com.EWIT.FrenchCafe.fragment.MainFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

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
        if (actionDrawerToggle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //        actionDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //        actionDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode){
                AlarmSetActivity.RESULT_CODE_ADD -> {
                    log("user add")
                    alarmListFragment.onAddedNewItem()
                }
            }
        }else{
            log("user cancel")
        }
    }

    override fun onResume() {
        super.onResume()
        loadAnimation()
    }

    /** Method zone **/

    private fun initInstance() {
        initToolbar()

        mainFragment = MainFragment.newInstance("mainFragment")
        alarmListFragment = AlarmListFragment.newInstance("alarmListFragment")

        replaceFragment(fragment = alarmListFragment)
        //        addFragment(R.id.contentContainer, mainFragment)

//        setupDrawerListener()
        Glide.with(this).load(R.drawable.wallpaper).into(ivParallaxImage)
        loadAnimation()
        btnAlarmActivity.setOnClickListener (onAlarmClickListener)
    }

    private fun loadAnimation() {
        var anim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_scale_up)
        btnAlarmActivity.startAnimation(anim)
    }

//    private fun switchFragmentDrawer(fragment: Fragment, item: MenuItem) {
//        replaceFragment(R.id.contentContainer, fragment)
//        with(item) {
//            this@MainActivity.title = title
//        }
//
//        drawerLayout.closeDrawers()
//    }

    private fun initToolbar() {
        //        actionDrawerToggle = setupDrawerToggle()
        setSupportActionBar(toolbar)

        //        drawerLayout.addDrawerListener(actionDrawerToggle)
//        navView.setCheckedItem(R.id.menu_main)
    }

//    private fun setupDrawerToggle(): ActionBarDrawerToggle {
//        return ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
//    }

    /** Listener zone **/

    val onAlarmClickListener = { view: View ->
        startActivityForResult(Intent(this@MainActivity, AlarmSetActivity::class.java), AlarmSetActivity.RESULT_CODE_ADD)
    }

//    private fun setupDrawerListener() {
//        navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.menu_main -> switchFragmentDrawer(mainFragment, it)
//                R.id.menu_alarm_list -> switchFragmentDrawer(alarmListFragment, it)
//            }
//            true
//        }
//    }
}
