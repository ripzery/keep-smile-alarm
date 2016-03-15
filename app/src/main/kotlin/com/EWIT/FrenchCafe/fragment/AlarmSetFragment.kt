package com.EWIT.FrenchCafe.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafe.dialog.TimePickerDialogFragment
import com.EWIT.FrenchCafe.extension.get
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.save
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.util.CalendarConverter
import com.EWIT.FrenchCafe.manager.MyNotificationManager
import com.EWIT.FrenchCafe.manager.SharePrefDaoManager
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.Contextor
import com.EWIT.FrenchCafe.util.SharePref
import kotlinx.android.synthetic.main.fragment_alarm_set.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class AlarmSetFragment : Fragment() {

    /** Variable zone **/
    lateinit var param1: String
    var currentDate: Model.DatePicked? = null
    var currentTime: Model.TimePicked? = null
    var alarmCollectionDao: Model.AlarmCollectionDao? = null

    /** static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): AlarmSetFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val alarmSetFragment: AlarmSetFragment = AlarmSetFragment()
            alarmSetFragment.arguments = bundle
            return alarmSetFragment
        }

    }

    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_alarm_set, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance() {
        btnPickDate.setOnClickListener { showDatePickerDialog() }
        btnPickTime.setOnClickListener { showTimePickerDialog() }
        btnSetAlarm.setOnClickListener { setAlarm() }
    }

    private fun showTimePickerDialog() {
        var timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.show(childFragmentManager, "timePicker");
        timePickerDialog.getTimePickedObservable().subscribe {
            currentTime = it;
            btnPickTime.text = it.getTimeFormat()
        }
    }

    private fun showDatePickerDialog() {
        var datePickerDialog = DatePickerDialogFragment()
        datePickerDialog.show(childFragmentManager, "datePicker");
        datePickerDialog.getDatePickedObservable().subscribe {
            currentDate = it
            btnPickDate.text = it.getDateFormat()
        }
    }

    private fun setAlarm() {
        if (isSelectedDateTime()) {
            /* get alarmCollectionDao */

            alarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()

            /* create new alarm dao */
            //TODO: Fix this
            var alarmDao: Model.AlarmDao = Model.AlarmDao(currentDate!!, currentTime!!, ArrayList<Int>())

            /* add alarm dao */
            alarmCollectionDao?.alarmCollectionList?.add(alarmDao)

            /* update alarm collection */
            updateAlarmCollectionDao()

            /* start alarm */
            startAlarmReceiver(alarmDao)
        }
    }

    private fun startAlarmReceiver(alarmDao: Model.AlarmDao) {
    //        broadcast notification

//        MyNotificationManager.broadcastNotificationIntent("Wakeup title",
//                "Wakeup description",
//                R.mipmap.icon,
//                alarmDate.timeInMillis,
//                alarmDao.hashCode().toLong())

        WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDao)

        toast("Set alarm at ${alarmDao.timePicked.getTimeFormat()}")

    }

    private fun updateAlarmCollectionDao() {
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
    }

    private fun isSelectedDateTime(): Boolean {
        if (currentDate == null) {
            toast("Please select date")
            return false
        }

        if (currentTime == null) {
            toast("Please select time")
            return false
        }

        return true
    }

}