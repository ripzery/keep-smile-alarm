package com.EWIT.FrenchCafe.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TimePicker
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.interfaces.AlarmSetInterface
import com.EWIT.FrenchCafe.model.dao.Model
import kotlinx.android.synthetic.main.fragment_maps_alarm.*
import kotlinx.android.synthetic.main.layout_repeat_day.*
import kotlinx.android.synthetic.main.layout_time_picker.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class SmartAlarmFragment : Fragment(), AlarmSetInterface {

    /** Variable zone **/
    lateinit var param1: String
    private val c: Calendar = Calendar.getInstance()
    private val hour = c.get(Calendar.HOUR_OF_DAY)
    private val minute = c.get(Calendar.MINUTE)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private val month = c.get(Calendar.MONTH)
    private val year = c.get(Calendar.YEAR)
    private var repeatDayList: List<Int> = listOf()
    lateinit private var currentDate: Model.DatePicked
    lateinit private var currentTime: Model.TimePicked
    lateinit var alarmDao: Model.AlarmDao
    private val mRrule: String? = ""


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : SmartAlarmFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val smartAlarmFragment: SmartAlarmFragment = SmartAlarmFragment()
            smartAlarmFragment.arguments = bundle
            return smartAlarmFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_smart_alarm, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
        
    }

    /** Override method zone **/

    override fun onAlarmStarted(alarmDao: Model.AlarmDao) {
        log(alarmDao.toString())
        toast("Set alarm at ${alarmDao.timePicked.getTimeFormat()}")
        activity.finish()
    }


    /** Method zone **/

    private fun initInstance(){

        currentDate = Model.DatePicked(year, month, day)
        currentTime = Model.TimePicked(hour, minute)

        repeatDayViewGroup.getCheckedDayObservable().map { it.sorted() }.subscribe {
            log(it.toString())
            repeatDayList = it
        }

        alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)

        tvDestTime.text = "${getString(R.string.fragment_maps_alarm_destination)} ${currentTime.getTimeFormat()}"


        time.setIs24HourView(false)

        time.setOnTimeChangedListener(timeChangedListener)

        btnSetAlarm.setOnClickListener(btnSetAlarmListener)

        cbRepeat.setOnClickListener(cbRepeatListener)

        cbRepeat.setOnClickListener(cbRepeatListener)
    }

    /** Listener zone **/

    val cbRepeatListener = { view: View ->
        if (cbRepeat.isChecked) {
            repeatDayViewGroup.visibility = View.VISIBLE
//            repeatDayViewGroup.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fade_in))
        } else {
            repeatDayViewGroup.visibility = View.GONE
//            repeatDayViewGroup.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fade_out))
        }
    }

    val btnSetAlarmListener = { view: View ->
        alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)
        setAlarm(alarmDao)
    }

    val timeChangedListener = { timePicker: TimePicker, hour: Int, min: Int ->
        currentTime = Model.TimePicked(hour, min)
        tvDestTime.text = "${getString(R.string.fragment_maps_alarm_destination)} ${currentTime.getTimeFormat()}"
    }
}