package com.socket9.eyealarm

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.socket9.eyealarm.extension.replaceFragment
import com.socket9.eyealarm.fragment.WakeTrackerFragment

class MainActivity : AppCompatActivity() {

    /** Variable zone **/


    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance() {

    }

    /** Listener zone **/

}
