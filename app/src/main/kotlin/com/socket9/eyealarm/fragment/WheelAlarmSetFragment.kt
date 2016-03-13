package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.view.animation.AnimationUtils
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.log
import com.socket9.eyealarm.extension.toast
import com.socket9.eyealarm.interfaces.AlarmSetInterface
import com.socket9.eyealarm.model.dao.Model
import kotlinx.android.synthetic.main.fragment_wheel_date_time.*
import kotlinx.android.synthetic.main.layout_date_time.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class WheelAlarmSetFragment : Fragment(), AlarmSetInterface {
    /** Override variable zone **/

    lateinit override var alarmDao: Model.AlarmDao

    /** Variable zone **/

    private val c: Calendar = Calendar.getInstance()
    private val hour = c.get(Calendar.HOUR_OF_DAY)
    private val minute = c.get(Calendar.MINUTE)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private val month = c.get(Calendar.MONTH)
    private val year = c.get(Calendar.YEAR)
    lateinit var param1: String
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

    /** Lifecycle method zone **/

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
            R.id.menuRecurrence -> buildRecurrenceDialog().show(childFragmentManager, "Recurrence Picker")
        }
        return true
    }

    /** Override method zone **/

    override fun onAlarmStarted(alarmDao: Model.AlarmDao) {
        log(alarmDao.toString())
        toast("Set alarm at ${alarmDao.timePicked.getTimeFormat()}")
        activity.finish()
    }

    override fun onRecurrenceSet(eventRecur: EventRecurrence) {
        //TODO: Implement when set recurrence

        log(EventRecurrenceFormatter.getRepeatString(activity, resources, eventRecur, true))
    }

    override fun onDateSet(datePicked: Model.DatePicked) {
        tvAlarmOn.text = "Pick date ${datePicked.getDateFormat()}"
    }

    /** Method zone **/

    private fun initInstance() {

        //        Glide.with(activity).load(R.drawable.wallpaper).into(ivBackground)
        btnSelectDate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale_up))

        currentDate = Model.DatePicked(year, month, day)
        currentTime = Model.TimePicked(hour, minute)

        alarmDao = Model.AlarmDao(currentDate, currentTime)

        tvAlarmOn.text = "Pick date ${currentDate.getDateFormat()}"
        tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"

        time.setIs24HourView(false)

        btnSelectDate.setOnClickListener { buildDateDialog().show(childFragmentManager, "DatePicker") }

        time.setOnTimeChangedListener({ timePicker, hour, min ->
            currentTime = Model.TimePicked(hour, min)
            tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"
        })

        btnSetAlarm.setOnClickListener { setAlarm() }
    }
}