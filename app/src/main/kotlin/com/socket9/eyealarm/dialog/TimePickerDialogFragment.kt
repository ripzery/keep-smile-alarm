package com.socket9.eyealarm.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import com.socket9.eyealarm.extension.log
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.Calendar

/**
 * Created by Euro on 3/10/16 AD.
 */

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener{

    val timePickedSubject : PublishSubject<TimePicked> = PublishSubject.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this@TimePickerDialogFragment, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        timePickedSubject.onNext(TimePicked(hourOfDay, minute))
    }

    fun getTimePickedObservable(): Observable<TimePicked>{
        return timePickedSubject.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    data class TimePicked(val hourOfDay: Int, val minute: Int){
        fun getTimeFormat() : String{
            return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

}