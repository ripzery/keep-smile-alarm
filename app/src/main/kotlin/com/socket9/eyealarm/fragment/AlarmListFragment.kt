package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.google.gson.Gson
import com.socket9.eyealarm.R
import com.socket9.eyealarm.adapter.RecyclerAdapter
import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import com.socket9.eyealarm.extension.save
import com.socket9.eyealarm.extension.toast
import com.socket9.eyealarm.manager.SharePrefDaoManager
import com.socket9.eyealarm.manager.WakeupAlarmManager
import com.socket9.eyealarm.model.dao.Model
import com.socket9.eyealarm.util.CalendarConverter
import com.socket9.eyealarm.util.SharePref
import kotlinx.android.synthetic.main.fragment_alarm_list.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class AlarmListFragment : Fragment() {

    /** Variable zone **/
    lateinit var param1: String
    lateinit private var alarmCollectionList: ArrayList<Model.AlarmDao>
    lateinit private var recyclerAdapter: RecyclerAdapter

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): AlarmListFragment {
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
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }

        alarmCollectionList = SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_alarm_list, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
        alarmCollectionList = SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList
        recyclerAdapter.setList(alarmCollectionList)

        /* show text empty alarm if empty list */
        tvEmptyAlarm.visibility = if(alarmCollectionList.isEmpty()) View.VISIBLE else View.GONE
    }

    /** Method zone **/

    private fun initInstance() {
        recyclerAdapter = RecyclerAdapter(alarmCollectionList, alarmInfoInteractionListener)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        tvEmptyAlarm.setOnClickListener {
            tvEmptyAlarm.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale_up_down))
        }
    }

    private fun updateAlarm(datePicked: Model.DatePicked, index: Int, it: Model.TimePicked) {
        var newAlarmDao = Model.AlarmDao(datePicked, it, ArrayList())

        /* cancel alarm */
        val oldWakeupTime = CalendarConverter.parseAlarmDao(alarmCollectionList[index]).timeInMillis
        WakeupAlarmManager.cancelAlarm(oldWakeupTime)

        /* update new alarmDao */
        alarmCollectionList[index] = newAlarmDao

        /* save to sharePref */
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(Model.AlarmCollectionDao(alarmCollectionList)))

        /* update card */
        recyclerAdapter.updateAtPosition(index)

        /* set new alarm */
        val newWakeupTime = CalendarConverter.parseAlarmDao(newAlarmDao).timeInMillis
        WakeupAlarmManager.broadcastWakeupAlarmIntent(newWakeupTime)

        /* show toast */
        toast("Set new alarm ${alarmCollectionList[index].datePicked.getDateFormat()}@${alarmCollectionList[index].timePicked.getTimeFormat()}")
    }

    private fun deleteAlarm(index: Int) {
        val toDeleteAlarmDao = alarmCollectionList[index]

        /* cancel alarm */
        WakeupAlarmManager.cancelAlarm(CalendarConverter.parseAlarmDao(toDeleteAlarmDao).timeInMillis)

        /* delete */
        alarmCollectionList.removeAt(index)

        /* remove card */
        recyclerAdapter.removeAtPosition(index)

        /* if empty, show empty text */
        if (alarmCollectionList.isEmpty()) {
            tvEmptyAlarm.visibility = View.VISIBLE
        }

        /* update to share pref */
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(Model.AlarmCollectionDao(alarmCollectionList)))

        /* show toast delete */
        toast("Delete alarm ${toDeleteAlarmDao.datePicked.getDateFormat()}@${toDeleteAlarmDao.timePicked.getTimeFormat()}")
    }

    private fun showTimePickerDialog(datePicked: Model.DatePicked, index: Int) {
        var timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.show(childFragmentManager, "timePicker");
        timePickerDialog.getTimePickedObservable().subscribe {
            updateAlarm(datePicked, index, it)
        }
    }

    private fun showDatePickerDialog(index: Int) {
        var datePickerDialog = DatePickerDialogFragment()
        datePickerDialog.show(childFragmentManager, "datePicker");
        datePickerDialog.getDatePickedObservable().subscribe {
            showTimePickerDialog(it, index)
        }
    }

    /** Listener zone **/

    var alarmInfoInteractionListener = object : RecyclerAdapter.AlarmInfoInteractionListener {
        override fun onDelete(index: Int) {
            deleteAlarm(index)
        }

        override fun onEdit(alarmDao: Model.AlarmDao, index: Int) {
            showDatePickerDialog(index)
        }
    }
}