package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.AlarmSetActivity
import com.EWIT.FrenchCafe.adapter.RecyclerAdapter
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.save
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.SharePref
import com.EWIT.FrenchCafe.util.WaketimeUtil
import com.google.gson.Gson
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
        } else {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AlarmSetActivity.RESULT_CODE_EDIT -> log("user edit")
            }
        } else {
            // user cancel
            log("user cancel")
        }
    }

    override fun onResume() {
        super.onResume()
        log("onResume : 4nd")
        //        alarmCollectionList = SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList
        //        recyclerAdapter.setList(alarmCollectionList)

        /* show text empty alarm if empty list */
        tvEmptyAlarm.visibility = if (alarmCollectionList.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onPause() {
        super.onPause()
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

    //    private fun updateAlarm(datePicked: Model.DatePicked, index: Int, it: Model.TimeWake) {
    //        var newAlarmDao = Model.AlarmDao(datePicked, it, ArrayList())
    //
    //        /* cancel alarm */
    //        WakeupAlarmManager.cancelAlarm(WaketimeUtil.calculationWaketimeSummation(alarmCollectionList[index]))
    //
    //        /* update new alarmDao */
    //        alarmCollectionList[index] = newAlarmDao
    //
    //        /* save to sharePref */
    //        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(Model.AlarmCollectionDao(alarmCollectionList)))
    //
    //        /* update card */
    //        recyclerAdapter.updateAtPosition(index)
    //
    //
    //        WakeupAlarmManager.broadcastWakeupAlarmIntent(newAlarmDao)
    //
    //        /* show toast */
    //        toast("Set new alarm ${alarmCollectionList[index].datePicked.getDateFormat()}@${alarmCollectionList[index].timeWake.getTimeFormat()}")
    //    }

    fun onAddedNewItem() {
        val newItem: Model.AlarmDao = SharePrefDaoManager.getAlarmCollectionDao().alarmCollectionList.takeLast(1)[0]
        alarmCollectionList.add(newItem)
        recyclerAdapter.addAtPosition(alarmCollectionList.lastIndex)

        //        recyclerAdapter.setList(alarmCollectionList)
    }

    private fun deleteAlarm(index: Int) {
        val toDeleteAlarmDao = alarmCollectionList[index]

        /* cancel alarm */
        WakeupAlarmManager.cancelAlarm(WaketimeUtil.calculationWaketimeSummation(toDeleteAlarmDao))

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
        toast("Delete alarm ${toDeleteAlarmDao.datePicked.getDateFormat()}@${toDeleteAlarmDao.timeWake.getTimeFormat()}")
    }

    //    private fun showTimePickerDialog(datePicked: Model.DatePicked, index: Int) {
    //        var timePickerDialog = TimePickerDialogFragment()
    //        timePickerDialog.show(childFragmentManager, "timePicker");
    //        timePickerDialog.getTimePickedObservable().subscribe {
    //            updateAlarm(datePicked, index, it)
    //        }
    //    }
    //
    //    private fun showDatePickerDialog(index: Int) {
    //        var datePickerDialog = DatePickerDialogFragment()
    //        datePickerDialog.show(childFragmentManager, "datePicker");
    //        datePickerDialog.getDatePickedObservable().subscribe {
    //            showTimePickerDialog(it, index)
    //        }
    //    }

    /** Listener zone **/

    var alarmInfoInteractionListener = object : RecyclerAdapter.AlarmInfoInteractionListener {
        override fun onDelete(index: Int) {
            deleteAlarm(index)
        }

        override fun onEdit(alarmDao: Model.AlarmDao, index: Int) {
            //            showDatePickerDialog(index)

            // start activity AlarmSetActivity
            log(alarmDao.toString())

            val intent: Intent = Intent(activity, AlarmSetActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putParcelable(AlarmSetActivity.EXTRA_ALARM_DAO, alarmDao)
            bundle.putInt(AlarmSetActivity.EXTRA_EDIT_INDEX, index)
            intent.putExtras(bundle)
            startActivityForResult(intent, AlarmSetActivity.RESULT_CODE_EDIT)
        }
    }
}