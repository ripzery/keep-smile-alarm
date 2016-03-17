package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.MapsActivity
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.toast
import com.EWIT.FrenchCafe.interfaces.AlarmSetInterface
import com.EWIT.FrenchCafe.model.dao.Model
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_smart_alarm.*
import kotlinx.android.synthetic.main.layout_repeat_day.*
import kotlinx.android.synthetic.main.layout_time_picker.*
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class SmartAlarmFragment : Fragment(), AlarmSetInterface {

    /** Variable zone **/
    val START_PLACE_PICKER_REQUEST = 4;
    val DESTINATION_PLACE_PICKER_REQUEST = 5;
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
    lateinit private var builder: PlacePicker.IntentBuilder

    private val mRrule: String? = ""


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: String): SmartAlarmFragment {
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
        if (savedInstanceState == null) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                START_PLACE_PICKER_REQUEST -> tvSetStart.text = PlacePicker.getPlace(activity, data).name
                DESTINATION_PLACE_PICKER_REQUEST -> tvSetDestination.text = PlacePicker.getPlace(activity, data).name
                MapsActivity.REQUEST_CODE_START -> tvSetStart.text = data!!.getStringExtra(MapsActivity.RETURN_INTENT_EXTRA_PLACE)
                MapsActivity.REQUEST_CODE_DEST -> tvSetDestination.text = data!!.getStringExtra(MapsActivity.RETURN_INTENT_EXTRA_PLACE)
            }
        }

    }

    /** Override method zone **/

    override fun onAlarmStarted(alarmDao: Model.AlarmDao) {
        log(alarmDao.toString())
        toast("Set alarm at ${alarmDao.timePicked.getTimeFormat()}")
        activity.finish()
    }

    /** Method zone **/

    private fun initInstance() {

        currentDate = Model.DatePicked(year, month, day)
        currentTime = Model.TimePicked(hour, minute)

        repeatDayViewGroup.getCheckedDayObservable().map { it.sorted() }.subscribe {
            log(it.toString())
            repeatDayList = it
        }

        builder  = PlacePicker.IntentBuilder();

        alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)

        tvDestTime.text = "${getString(R.string.fragment_maps_alarm_destination)} ${currentTime.getTimeFormat()}"

        time.setIs24HourView(false)

        time.setOnTimeChangedListener(timeChangedListener)

        btnSetAlarm.setOnClickListener(btnSetAlarmListener)

        cbRepeat.setOnClickListener(cbRepeatListener)

        cbRepeat.setOnClickListener(cbRepeatListener)

        btnSetDest.setOnClickListener(btnSetDestListener)

        btnSetStart.setOnClickListener(btnSetStartListener)
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

    val btnSetStartListener = { view: View ->
        startActivityForResult(builder.build(activity), START_PLACE_PICKER_REQUEST);

//        startActivityForResult(Intent(activity, MapsActivity::class.java).putExtra(MapsActivity.EXTRA_TOOLBAR_TITLE, "Pick Start"),
//                MapsActivity.REQUEST_CODE_START)
    }

    val btnSetDestListener = { view: View ->
        startActivityForResult(builder.build(activity), DESTINATION_PLACE_PICKER_REQUEST)
//        startActivityForResult(Intent(activity, MapsActivity::class.java).putExtra(MapsActivity.EXTRA_TOOLBAR_TITLE, "Pick Destination"),
//                MapsActivity.REQUEST_CODE_DEST)
    }
}