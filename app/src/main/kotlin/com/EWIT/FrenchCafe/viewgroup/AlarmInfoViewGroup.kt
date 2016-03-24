package com.EWIT.FrenchCafe.viewgroup

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.manager.WakeupAlarmManager
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.WaketimeUtil
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.viewgroup_alarm_info.view.*
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class AlarmInfoViewGroup : BaseCustomViewGroup {

    /** Variable zone **/

    lateinit private var alarmDao: Model.AlarmDao
    lateinit private var containerView: View
    lateinit private var tvDate: TextView
    lateinit private var tvTime: TextView
    private var publishEditSubject: PublishSubject<Model.AlarmDao> = PublishSubject.create()
    private var publishDeleteSubject: PublishSubject<Int> = PublishSubject.create()
    private var publishSwitchSubject: PublishSubject<Pair<Model.AlarmDao, Boolean>> = PublishSubject.create()

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
        containerView = inflate(context, R.layout.viewgroup_alarm_info, this)
    }

    private fun initInstances() {
        // findViewById here
        //        tvDate = containerView.findViewById(R.id.tvDate) as TextView
        tvTime = containerView.findViewById(R.id.tvTime) as TextView

        cardView.setOnClickListener { publishEditSubject.onNext(alarmDao) }
        btnDelete.setOnClickListener { publishDeleteSubject.onNext(1) }

        btnSwitch.setOnCheckedChangeListener { compoundBtn: CompoundButton, isChecked: Boolean ->
            when (isChecked) {
                true -> WakeupAlarmManager.broadcastWakeupAlarmIntent(alarmDao)
                false -> {
                    /* cancel alarm */
                    WakeupAlarmManager.cancelAlarm(WaketimeUtil.calculationWaketimeSummation(alarmDao))
                }
            }
            publishSwitchSubject.onNext(Pair(alarmDao, isChecked))
        }

        switchRepeatDay.setOnCheckedChangeListener({ cb: CompoundButton, isChecked: Boolean ->
            repeatDay.visibility = if (isChecked) View.VISIBLE else View.GONE
        })
    }

    fun setAlarmDao(alarmDao: Model.AlarmDao) {
        this.alarmDao = alarmDao

        setupWaketime(alarmDao)

        setupPlace(alarmDao)

        setupRepeatDayToggle(alarmDao)

    }

    private fun setupWaketime(alarmDao: Model.AlarmDao) {
        /* manage text wake time */
        tvTime.text = alarmDao.timeWake.getTimeFormat()
    }

    private fun setupRepeatDayToggle(alarmDao: Model.AlarmDao) {
        /* manage repeat day toggle visibility */
        if (alarmDao.repeatDay.size > 0) {
            layoutRepeatDay.visibility = View.VISIBLE
            repeatDay.resetCheckedDay()
            repeatDay.setCheckedDay(alarmDao.repeatDay)
        } else {
            layoutRepeatDay.visibility = View.GONE
        }
    }

    private fun setupPlace(alarmDao: Model.AlarmDao) {
        /* manage place name */
        if (alarmDao.placePicked != null) {
            layoutArrival.visibility = View.VISIBLE
            tvArrivalPlace.text = "${alarmDao.placePicked!!.arrivalPlace.name}"
            tvArrivalTime.text = "Arrival at ${alarmDao.placePicked!!.getReadableTime(alarmDao.placePicked!!.arriveTime)}"
        } else {
            layoutArrival.visibility = View.GONE
        }
    }

    fun getEditObservable(): Observable<Model.AlarmDao> {
        return publishEditSubject.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getDeleteObservable(): Observable<Int> {
        return publishDeleteSubject.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getSwitchObservable(): Observable<Pair<Model.AlarmDao, Boolean>> {
        return publishSwitchSubject.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
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
