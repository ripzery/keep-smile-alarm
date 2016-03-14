package com.socket9.eyealarm.interfaces

import android.os.Bundle
import android.text.format.Time
import android.util.Log
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment
import com.google.gson.Gson
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
 * Created by Euro on 3/13/16 AD.
 */


interface AlarmSetInterface {

    /** Override method zone **/

    fun onAlarmStarted(alarmDao: Model.AlarmDao)

    fun onDateSet(datePicked: Model.DatePicked)

    fun onRecurrenceSet(eventRecur: EventRecurrence)

    /** Method zone **/

    fun setAlarm(alarmDao: Model.AlarmDao) {
        var alarmDate = CalendarConverter.parseAlarmDao(alarmDao)

        /* update alarm collection in share preference */
        updateAlarmCollectionDao(alarmDao)

        /* start alarm */
        startAlarmReceiver(alarmDate, alarmDao)
    }

    fun buildDateDialog(): CalendarDatePickerDialogFragment {
        var cdp = CalendarDatePickerDialogFragment()
            .setOnDateSetListener { calendarDatePickerDialogFragment: CalendarDatePickerDialogFragment, year: Int, month: Int, day: Int ->
                onDateSet(Model.DatePicked(year, month + 1, day))
            }
            .setFirstDayOfWeek(Calendar.SUNDAY)

        return cdp
    }

    fun buildRecurrenceDialog() : RecurrencePickerDialogFragment {
        var bundle = Bundle();
        val rule = ""
        var time = Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, rule);
        bundle.putBoolean(RecurrencePickerDialogFragment.BUNDLE_HIDE_SWITCH_BUTTON, true);

        var rpd = RecurrencePickerDialogFragment();
        rpd.arguments = bundle;
        rpd.setOnRecurrenceSetListener({
            if (it != null) {
                var eventRecur = EventRecurrence()
                eventRecur.parse(it)
                onRecurrenceSet(eventRecur)
            }
        });

        return rpd
    }

    /** Internal method zone **/

    private fun startAlarmReceiver(alarmDate: GregorianCalendar, alarmDao: Model.AlarmDao) {
        //        broadcast notification

        //        MyNotificationManager.broadcastNotificationIntent("Wakeup title",
        //                "Wakeup description",
        //                R.mipmap.icon,
        //                alarmDate.timeInMillis,
        //                alarmDao.hashCode().toLong())

        WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDate.timeInMillis)

        onAlarmStarted(alarmDao)
    }

    private fun updateAlarmCollectionDao(alarmDao: Model.AlarmDao) {
        /* get alarmCollectionDao */
        var alarmCollectionDao = SharePrefDaoManager.getAlarmCollectionDao()

        /* add alarm dao */
        alarmCollectionDao.alarmCollectionList.add(alarmDao)

        /* save back to share preference */
        save(SharePref.SHARE_PREF_KEY_ALARM_COLLECTION_JSON, Gson().toJson(alarmCollectionDao))
    }

}