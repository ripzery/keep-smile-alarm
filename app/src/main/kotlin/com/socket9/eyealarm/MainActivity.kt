package com.socket9.eyealarm

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.socket9.eyealarm.extension.log
import com.socket9.eyealarm.extension.replaceFragment
import com.socket9.eyealarm.fragment.MainFragment
import com.socket9.eyealarm.fragment.TemplateFragment
import com.socket9.eyealarm.fragment.WakeTrackerFragment
import com.socket9.eyealarm.util.SharePref

class MainActivity : AppCompatActivity() {

    /** Variable zone **/
    lateinit private var mainFragment: MainFragment


    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInstance()
    }


    /** Method zone **/

    private fun initInstance() {
        mainFragment = MainFragment.newInstance("mainFragment")
        replaceFragment(R.id.contentContainer, mainFragment)

        if(SharePref.sharePref?.getString("boot", "fail").equals("complete")){
            log("Yayyyy boot receiver working")
        }
    }

    /** Listener zone **/

}
