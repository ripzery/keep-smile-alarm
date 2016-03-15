package com.EWIT.FrenchCafe.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.AlarmSetActivity
import com.EWIT.FrenchCafe.activity.WakeTrackerActivity
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class MainFragment : Fragment(){
    /** Variable zone **/

    lateinit var param1: String


    /** Static method zone **/
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

    /** Lifecycle zone **/

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

    override fun onResume() {
        super.onResume()
        loadAnimation()
    }

    /** Method zone **/

    private fun initInstance() {
//        btnWakeActivity.setOnClickListener { startActivity(Intent(activity, WakeTrackerActivity::class.java)) }
        loadAnimation()
        btnAlarmActivity.setOnClickListener { startActivity(Intent(activity, AlarmSetActivity::class.java)) }
    }

    private fun loadAnimation(){
        var anim = AnimationUtils.loadAnimation(activity, R.anim.anim_scale_up)
        btnAlarmActivity.startAnimation(anim)
    }
}