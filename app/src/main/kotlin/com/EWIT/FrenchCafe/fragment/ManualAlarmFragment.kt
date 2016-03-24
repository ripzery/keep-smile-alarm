package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.AlarmSetActivity
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.mCalendar
import com.EWIT.FrenchCafe.extension.toDatePicked
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.interfaces.AlarmSetInterface
import com.EWIT.FrenchCafe.model.dao.Model
import kotlinx.android.synthetic.main.fragment_manual_alarm.*
import kotlinx.android.synthetic.main.layout_choose_alarm_sound.*
import kotlinx.android.synthetic.main.layout_repeat_day.*
import kotlinx.android.synthetic.main.layout_time_picker.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class ManualAlarmFragment : Fragment(), AlarmSetInterface {
    override var alarmSoundUri: Uri? = null
    private val c: Calendar = Calendar.getInstance()
    private val hour = c.get(Calendar.HOUR_OF_DAY)
    private val minute = c.get(Calendar.MINUTE)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private val month = c.get(Calendar.MONTH)
    private val year = c.get(Calendar.YEAR)
    private var repeatDayList: List<Int> = listOf()
    lateinit private var currentDate: Model.DatePicked
    lateinit private var currentTime: Model.TimeWake
    private var alarmDao: Model.AlarmDao? = null
    private var editIndex: Int = -1
    private val mRrule: String? = ""

    /** Static method zone **/

    companion object {
        val ARG_1 = "ARG_1"
        val ARG_2 = "ARG_2"
        val REQUEST_CODE_CHOOSE_ALARM_SOUND = 10

        fun newInstance(alarmDao: Model.AlarmDao?, editIndex: Int): ManualAlarmFragment {
            var bundle: Bundle = Bundle()
            bundle.putParcelable(ARG_1, alarmDao)
            bundle.putInt(ARG_2, editIndex)
            val manualAlarmFragment: ManualAlarmFragment = ManualAlarmFragment()
            manualAlarmFragment.arguments = bundle
            return manualAlarmFragment
        }

    }

    /** Lifecycle method zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            alarmDao = arguments.getParcelable<Model.AlarmDao>(ARG_1)
            editIndex = arguments.getInt(ARG_2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_manual_alarm, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CHOOSE_ALARM_SOUND -> {
                    alarmSoundUri = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    tvAlarmSound.text = RingtoneManager.getRingtone(context, alarmSoundUri).getTitle(activity)
                }
            }
        }
    }

    /** Override method zone **/

    override fun onAlarmStarted(alarmDao: Model.AlarmDao) {
        log(alarmDao.toString())
        toast("Set alarm at ${alarmDao.timeWake.getTimeFormat()}")
        val data: Intent = Intent()
        data.putExtra(AlarmSetActivity.EXTRA_EDIT_INDEX, editIndex)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    /** Method zone **/

    private fun initInstance() {

        //        Glide.with(activity).load(R.drawable.wallpaper).into(ivBackground)
        //        btnSelectDate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale_up))

        if (alarmDao != null) {

            initEditData(alarmDao!!)

        } else {
            currentDate = Model.DatePicked(year, month, day)
            currentTime = Model.TimeWake(hour, minute)
            alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)
        }


        repeatDayViewGroup.getCheckedDayObservable().map { it.sorted() }.subscribe {
            log(it.toString())
            repeatDayList = it
        }

        tvAlarmOn.text = "Pick date ${currentDate.getDateFormat()}"
        tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"

        time.setIs24HourView(false)

        time.setOnTimeChangedListener(timeChangedListener)

        btnChooseRingtone.setOnClickListener(chooseAlarmListener)

        btnSetAlarm.setOnClickListener(btnSetAlarmListener)

        cbRepeat.setOnClickListener(cbRepeatListener)
    }

    private fun toggleRepeatDayVisible() {
        if (cbRepeat.isChecked) {
            repeatDayViewGroup.visibility = View.VISIBLE
            //            repeatDayViewGroup.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fade_in))
        } else {
            repeatDayViewGroup.visibility = View.GONE
            repeatDayViewGroup.resetCheckedDay()
            //            repeatDayViewGroup.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fade_out))
        }
    }

    private fun initEditData(alarmDao: Model.AlarmDao) {
        time.setCurrentHour(alarmDao.timeWake.hourOfDay)
        //        time.hour = alarmDao.timeWake.hourOfDay
        time.setCurrentMinute(alarmDao.timeWake.minute)
        log("takeEffect!")
        currentDate = mCalendar().toDatePicked()
        currentTime = alarmDao.timeWake

        if (alarmDao.alarmSound != null) {
            alarmSoundUri = Uri.parse(alarmDao.alarmSound)
            tvAlarmSound.text = RingtoneManager.getRingtone(activity, alarmSoundUri).getTitle(activity)
        }

        if (alarmDao.repeatDay.size != 0) {
            cbRepeat.isChecked = true
            repeatDayList = alarmDao.repeatDay
            repeatDayViewGroup.setCheckedDay(alarmDao.repeatDay)
        } else {
            cbRepeat.isChecked = false
        }

        toggleRepeatDayVisible()

    }

    /** Listener zone **/

    val cbRepeatListener = { view: View ->
        toggleRepeatDayVisible()
    }

    val btnSetAlarmListener = { view: View ->
        alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)
        if (editIndex == -1) {
            setAlarm(alarmDao!!)
        } else {
            updateAlarm(alarmDao!!, editIndex)
        }
    }

    val timeChangedListener = { timePicker: TimePicker, hour: Int, min: Int ->
        currentTime = Model.TimeWake(hour, min)
        tvPickTime.text = "Pick time ${currentTime.getTimeFormat()}"
    }

    val chooseAlarmListener = { view: View ->
        val intent: Intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
        this.startActivityForResult(intent, REQUEST_CODE_CHOOSE_ALARM_SOUND);
    }
}