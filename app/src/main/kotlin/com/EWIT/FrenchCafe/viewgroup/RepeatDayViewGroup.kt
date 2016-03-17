package com.EWIT.FrenchCafe.viewgroup

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import com.EWIT.FrenchCafe.R
import kotlinx.android.synthetic.main.viewgroup_day_repeat.view.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject
import java.util.*

class RepeatDayViewGroup : BaseCustomViewGroup {

    /** Variable zone **/
    lateinit private var containerView: View
    private var listCheckedDay = ArrayList<Int>()
    lateinit private var observableCheckedDay: PublishSubject<ArrayList<Int>>


    /** Constructor zone **/

    constructor(context: Context) : super(context) {
        initInflate()
        initInstances()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, defStyleRes)

    }


    /** Method zone **/

    private fun initInflate() {
        containerView = inflate(context, R.layout.viewgroup_day_repeat, this)
    }

    private fun initInstances() {
        // findViewById here
        if (!isInEditMode) {
            observableCheckedDay = PublishSubject.create()
        }

        btnSun.setOnCheckedChangeListener(onCheckedChangeListener)
        btnMon.setOnCheckedChangeListener(onCheckedChangeListener)
        btnTue.setOnCheckedChangeListener(onCheckedChangeListener)
        btnWed.setOnCheckedChangeListener(onCheckedChangeListener)
        btnThu.setOnCheckedChangeListener(onCheckedChangeListener)
        btnFri.setOnCheckedChangeListener(onCheckedChangeListener)
        btnSat.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    private fun initWithAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    fun getListCheckedDay(): ArrayList<Int> {
        return listCheckedDay
    }

    fun getCheckedDayObservable(): Observable<ArrayList<Int>> {
        return observableCheckedDay.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun addOrRemoveListCheckedDay(day: Int, isChecked: Boolean) {
        if (isChecked) {
            listCheckedDay.add(day)
        } else {
            listCheckedDay.remove(day)
        }

        observableCheckedDay.onNext(listCheckedDay)
    }

    /** Listener zone **/

    var onCheckedChangeListener = { toggleBtn: CompoundButton, isChecked: Boolean ->
        when (toggleBtn.id) {
            R.id.btnSun -> addOrRemoveListCheckedDay(Calendar.SUNDAY, isChecked)
            R.id.btnMon -> addOrRemoveListCheckedDay(Calendar.MONDAY, isChecked)
            R.id.btnTue -> addOrRemoveListCheckedDay(Calendar.TUESDAY, isChecked)
            R.id.btnWed -> addOrRemoveListCheckedDay(Calendar.WEDNESDAY, isChecked)
            R.id.btnThu -> addOrRemoveListCheckedDay(Calendar.THURSDAY, isChecked)
            R.id.btnFri -> addOrRemoveListCheckedDay(Calendar.FRIDAY, isChecked)
            R.id.btnSat -> addOrRemoveListCheckedDay(Calendar.SATURDAY, isChecked)
        }

    }
}
