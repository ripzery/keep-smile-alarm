package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.Time
import android.view.*
import android.view.animation.AnimationUtils
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment
import com.google.gson.Gson
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.log
import com.socket9.eyealarm.extension.save
import com.socket9.eyealarm.extension.toast
import com.socket9.eyealarm.manager.SharePrefDaoManager
import com.socket9.eyealarm.manager.WakeupAlarmManager
import com.socket9.eyealarm.model.dao.Model
import com.socket9.eyealarm.util.CalendarConverter
import com.socket9.eyealarm.util.SharePref
import kotlinx.android.synthetic.main.fragment_wheel_date_time.*
import kotlinx.android.synthetic.main.layout_date_time.*
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
    private val mRrule: String? = ""


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
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_wheel_date_time, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_set_alarm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuRecurrence -> showRecurrenceDialog()
        }
        return true
    }

    /** Method zone **/

    private fun initInstance() {

        //        Glide.with(activity).load(R.drawable.wallpaper).into(ivBackground)

        btnSelectDate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale_up))

        val c: Calendar = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        currentDate = Model.DatePicked(year, month, day)
        currentTime = Model.TimePicked(hour, minute)

        tvAlarmOn.text = "Pick date ${currentDate.getDateFormat()}"
        tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"


        time.setIs24HourView(false)

        btnSelectDate.setOnClickListener {
            var cdp = CalendarDatePickerDialogFragment()
                    .setOnDateSetListener { calendarDatePickerDialogFragment: CalendarDatePickerDialogFragment, year: Int, month: Int, day: Int ->
                        currentDate = Model.DatePicked(year, month + 1, day)
                        tvAlarmOn.text = "Pick date ${currentDate.getDateFormat()}"
                    }
                    .setFirstDayOfWeek(Calendar.SUNDAY)

            cdp.show(childFragmentManager, "DatePicker")
        }

        time.setOnTimeChangedListener({ timePicker, hour, min ->
            currentTime = Model.TimePicked(hour, min)
            tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"
        })


        btnSetAlarm.setOnClickListener { setAlarm() }
    }

    private fun showRecurrenceDialog() {
        var bundle = Bundle();
        var time = Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);
        bundle.putBoolean(RecurrencePickerDialogFragment.BUNDLE_HIDE_SWITCH_BUTTON, true);

        var rpd = RecurrencePickerDialogFragment();
        rpd.arguments = bundle;
        rpd.setOnRecurrenceSetListener({
            if (it != null) {
                var eventRecur = EventRecurrence()
                eventRecur.parse(it)
                log(EventRecurrenceFormatter.getRepeatString(activity, resources, eventRecur, true))
            }
        });
        rpd.show(childFragmentManager, "Recurrence Picker");
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

        activity.finish()
    }

    private fun updateAlarmCollectionDao() {
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
    }
}