package com.socket9.eyealarm.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import com.socket9.eyealarm.extension.log
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.Calendar

/**
 * Created by Euro on 3/10/16 AD.
 */
class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{
    val datePickedSubject : PublishSubject<DatePickerDialogFragment.DatePicked> = PublishSubject.create()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()

        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        return DatePickerDialog(activity, this@DatePickerDialogFragment, year, month, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        datePickedSubject.onNext(DatePicked(year, monthOfYear, dayOfMonth))
    }

    fun getDatePickedObservable(): Observable<DatePicked>{
        return datePickedSubject.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }


    data class DatePicked(val year: Int, val monthOfYear: Int, val dayOfMonth: Int){
        fun getDateFormat(): String{
            return String.format("%02d", dayOfMonth) +"/"+ String.format("%02d", monthOfYear) + "/" + year
        }
    }

}