package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.gson.Gson
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.save
import com.socket9.eyealarm.extension.toast
import com.socket9.eyealarm.manager.SharePrefDaoManager
import com.socket9.eyealarm.manager.WakeupAlarmManager
import com.socket9.eyealarm.model.dao.Model
import com.socket9.eyealarm.util.CalendarConverter
import com.socket9.eyealarm.util.SharePref
import kotlinx.android.synthetic.main.fragment_wheel_date_time.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class WheelAlarmSetFragment : Fragment() {

    /** Variable zone **/
    lateinit var param1: String
    lateinit private var alarmCollectionDao: Model.AlarmCollectionDao
    lateinit private var currentDate: Model.DatePicked
    lateinit private var currentTime: Model.TimePicked


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): WheelAlarmSetFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val wheelAlarmSetFragment: WheelAlarmSetFragment = WheelAlarmSetFragment()
            wheelAlarmSetFragment.arguments = bundle
            return wheelAlarmSetFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_wheel_date_time, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    /** Method zone **/

    private fun initInstance() {
        val c: Calendar = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        currentDate = Model.DatePicked(year, month, day)
        currentTime = Model.TimePicked(hour, minute)

        time.setIs24HourView(false)
        date.init(year, month, day, { datePicker, year, month, day ->
            currentDate = Model.DatePicked(year, month, day)
        })
        time.setOnTimeChangedListener({ timePicker, hour, min ->
            currentTime = Model.TimePicked(hour, min)
        })

        btnSetAlarm.setOnClickListener { setAlarm() }
    }



    private fun setAlarm() {
        var alarmDate = CalendarConverter.parseDateTimePicked(currentDate, currentTime)

        /* get alarmCollectionDao */

        alarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()

        /* create new alarm dao */
        var alarmDao: Model.AlarmDao = Model.AlarmDao(currentDate, currentTime)

        /* add alarm dao */
        alarmCollectionDao.alarmCollectionList.add(alarmDao)

        /* update alarm collection */
        updateAlarmCollectionDao()

        /* start alarm */
        startAlarmReceiver(alarmDate, alarmDao)
    }

    private fun startAlarmReceiver(alarmDate: GregorianCalendar, alarmDao: Model.AlarmDao) {
        //        broadcast notification

        //        MyNotificationManager.broadcastNotificationIntent("Wakeup title",
        //                "Wakeup description",
        //                R.mipmap.icon,
        //                alarmDate.timeInMillis,
        //                alarmDao.hashCode().toLong())

        WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDate.timeInMillis)

        toast("Set alarm at ${alarmDao.timePicked.getTimeFormat()}")

    }

    private fun updateAlarmCollectionDao() {
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
    }
}