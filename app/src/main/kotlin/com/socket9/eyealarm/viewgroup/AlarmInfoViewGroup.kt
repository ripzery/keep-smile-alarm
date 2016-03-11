package com.socket9.eyealarm.viewgroup

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.socket9.eyealarm.R
import com.socket9.eyealarm.model.dao.Model
import kotlinx.android.synthetic.main.viewgroup_alarm_info.view.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class AlarmInfoViewGroup : BaseCustomViewGroup {
    lateinit private var alarmDao: Model.AlarmDao
    lateinit private var containerView: View
    lateinit private var tvDate: TextView
    lateinit private var tvTime: TextView
    private var publishEditSubject: PublishSubject<Model.AlarmDao> = PublishSubject.create()
    private var publishDeleteSubject: PublishSubject<Int> = PublishSubject.create()

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

    private fun initInflate() {
        containerView = inflate(context, R.layout.viewgroup_alarm_info, this)
    }

    private fun initInstances() {
        // findViewById here
        tvDate = containerView.findViewById(R.id.tvDate) as TextView
        tvTime = containerView.findViewById(R.id.tvTime) as TextView

        btnEdit.setOnClickListener { publishEditSubject.onNext(alarmDao) }
        btnDelete.setOnClickListener {
            //remove this card
            publishDeleteSubject.onNext(1)
        }
    }

    fun setAlarmDao(alarmDao: Model.AlarmDao) {
        this.alarmDao = alarmDao

        tvDate.text = alarmDao.datePicked.getDateFormat()
        tvTime.text = alarmDao.timePicked.getTimeFormat()
    }

    fun getEditObservable() : Observable<Model.AlarmDao> {
        return publishEditSubject.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getDeleteObservable(): Observable<Int> {
        return publishDeleteSubject.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
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
}
