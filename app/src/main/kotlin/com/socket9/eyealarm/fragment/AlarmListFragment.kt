package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.socket9.eyealarm.R
import com.socket9.eyealarm.adapter.RecyclerAdapter
import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import com.socket9.eyealarm.extension.log
import com.socket9.eyealarm.extension.save
import com.socket9.eyealarm.extension.toast
import com.socket9.eyealarm.manager.SharePrefDaoManager
import com.socket9.eyealarm.model.dao.Model
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
        recyclerAdapter = RecyclerAdapter(alarmCollectionList, alarmInfoInteractionListener)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    /** Method zone **/

    private fun showTimePickerDialog(datePicked: DatePickerDialogFragment.DatePicked, index: Int) {
        var timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.show(childFragmentManager, "timePicker");
        timePickerDialog.getTimePickedObservable().subscribe {
            var newAlarmDao = Model.AlarmDao(datePicked, it)

            /* update new alarmDao */

            alarmCollectionList[index] = newAlarmDao

            /* save to sharePref */
            save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(Model.AlarmCollectionDao(alarmCollectionList)))

            /* update card */
            recyclerAdapter.updateAtPosition(index, newAlarmDao)

            /* cancel alarm */


            /* show toast */
            toast("Set new alarm ${alarmCollectionList[index].datePicked.getDateFormat()}@${alarmCollectionList[index].timePicked.getTimeFormat()}")
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

            log("$index")
        }

        override fun onEdit(alarmDao: Model.AlarmDao, index: Int) {
            showDatePickerDialog(index)
        }
    }
}