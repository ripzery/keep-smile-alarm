package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.eyealarm.R
import com.socket9.eyealarm.adapter.RecyclerAdapter
import com.socket9.eyealarm.manager.SharePrefDaoManager
import kotlinx.android.synthetic.main.fragment_alarm_list.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class AlarmListFragment : Fragment(){

    /** Variable zone **/
    lateinit var param1: String


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : AlarmListFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val alarmListFragment: AlarmListFragment = AlarmListFragment()
            alarmListFragment.arguments = bundle
            return alarmListFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_alarm_list, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = RecyclerAdapter(SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    /** Method zone **/
}