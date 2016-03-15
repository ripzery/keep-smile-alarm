package com.EWIT.FrenchCafe.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.model.dao.Model
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.Calendar

/**
 * Created by Euro on 3/10/16 AD.
 */

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener{

    val timePickedSubject : PublishSubject<Model.TimePicked> = PublishSubject.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this@TimePickerDialogFragment, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        timePickedSubject.onNext(Model.TimePicked(hourOfDay, minute))
    }

    fun getTimePickedObservable(): Observable<Model.TimePicked>{
        return timePickedSubject.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

}
