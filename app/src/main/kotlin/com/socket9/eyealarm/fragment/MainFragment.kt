package com.socket9.eyealarm.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.eyealarm.R
import com.socket9.eyealarm.activity.AlarmSetActivity
import com.socket9.eyealarm.activity.WakeTrackerActivity
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class MainFragment : Fragment(){
    lateinit var param1: String

    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : MainFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val mainFragment: MainFragment = MainFragment()
            mainFragment.arguments = bundle
            return mainFragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_main, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstance()
    }

    private fun initInstance() {
        btnWakeActivity.setOnClickListener { startActivity(Intent(activity, WakeTrackerActivity::class.java)) }

        btnSetAlarm.setOnClickListener { startActivity(Intent(activity, AlarmSetActivity::class.java)) }

    }
}