package com.EWIT.FrenchCafé.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.EWIT.FrenchCafé.R
import com.EWIT.FrenchCafé.dialog.DatePickerDialogFragment
import com.EWIT.FrenchCafé.dialog.TimePickerDialogFragment
import com.EWIT.FrenchCafé.extension.get
import com.EWIT.FrenchCafé.extension.log
import com.EWIT.FrenchCafé.extension.save
import com.EWIT.FrenchCafé.extension.toast
import com.EWIT.FrenchCafé.util.CalendarConverter
import com.EWIT.FrenchCafé.manager.MyNotificationManager
import com.EWIT.FrenchCafé.manager.SharePrefDaoManager
import com.EWIT.FrenchCafé.manager.WakeupAlarmManager
import com.EWIT.FrenchCafé.model.dao.Model
import com.EWIT.FrenchCafé.util.Contextor
import com.EWIT.FrenchCafé.util.SharePref
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