package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.AlarmSetActivity
import com.EWIT.FrenchCafe.extension.*
import com.EWIT.FrenchCafe.interfaces.AlarmSetInterface
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.model.dao.NetworkModel
import com.EWIT.FrenchCafe.network.HttpManager
import com.EWIT.FrenchCafe.util.WaketimeUtil
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_smart_alarm.*
import kotlinx.android.synthetic.main.layout_repeat_day.*
import kotlinx.android.synthetic.main.layout_time_picker.*
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class SmartAlarmFragment : Fragment(), AlarmSetInterface {

    /** Variable zone **/
    val START_PLACE_PICKER_REQUEST = 4;
    val DESTINATION_PLACE_PICKER_REQUEST = 5;

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
    lateinit private var builder: PlacePicker.IntentBuilder
    private var travelInfoSubscription: Subscription? = null
    private var startPlace: Model.PlaceDetail? = null
    private var isEnabledLocation: Boolean = false
    private var destPlace: Model.PlaceDetail? = null
    private var editIndex: Int = -1
    private val mRrule: String? = ""
    private val locationSettingObservable = PublishSubject.create<Boolean>()
    lateinit private var locationProvider: ReactiveLocationProvider
    private val locationRequest: LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)
            .setInterval(1000)
    private val locationSetting: LocationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()


    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"
        val ARG_2 = "ARG_2"
        val PERSONAL_TIME_OFFSET = 60 * 60 * 1000
        val CHECK_LOCATION_SETTING_REQUEST = 6


        fun newInstance(param1: Model.AlarmDao?, editIndex: Int): SmartAlarmFragment {
            var bundle: Bundle = Bundle()
            bundle.putParcelable(ARG_1, param1)
            bundle.putInt(ARG_2, editIndex)
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
            locationProvider = ReactiveLocationProvider(context)
            alarmDao = arguments.getParcelable<Model.AlarmDao>(ARG_1)
            editIndex = arguments.getInt(ARG_2)
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
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                START_PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(activity, data)
                    startPlace = Model.PlaceDetail(place.id, place.name.toString(), Model.PlaceLatLng(place.latLng.latitude, place.latLng.longitude))
                    tvSetStart.text = startPlace?.name
                    //                    checkedPickLocation()
                }
                DESTINATION_PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(activity, data)
                    destPlace = Model.PlaceDetail(place.id, place.name.toString(), Model.PlaceLatLng(place.latLng.latitude, place.latLng.longitude))
                    tvSetDestination.text = destPlace?.name
                    //                    checkedPickLocation()
                }
            //                MapsActivity.REQUEST_CODE_START -> tvSetStart.text = data!!.getStringExtra(MapsActivity.RETURN_INTENT_EXTRA_PLACE)
            //                MapsActivity.REQUEST_CODE_DEST -> tvSetDestination.text = data!!.getStringExtra(MapsActivity.RETURN_INTENT_EXTRA_PLACE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        travelInfoSubscription?.unsubscribe()
    }

    /** Override method zone **/

    fun onLocationSettingResult(isAccept: Boolean){
        isEnabledLocation = isAccept
        locationSettingObservable.onNext(isEnabledLocation)
        if(!isEnabledLocation) toast("Please enable location setting")
    }

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

        builder = PlacePicker.IntentBuilder();

        tvDestTime.text = "${getString(R.string.fragment_maps_alarm_destination)} ${currentTime.getTimeFormat()}"

        time.setIs24HourView(false)

        // TODO : Init your location and show progress view in start

        time.setOnTimeChangedListener(timeChangedListener)

        btnSetAlarm.setOnClickListener(btnSetAlarmListener)

        cbRepeat.setOnClickListener(cbRepeatListener)

        cbRepeat.setOnClickListener(cbRepeatListener)

        btnSetDest.setOnClickListener(btnSetDestListener)

        btnSetStart.setOnClickListener(btnSetStartListener)
    }

    private fun initEditData(alarmDao: Model.AlarmDao) {
        currentDate = mCalendar().toDatePicked()
        currentTime = Calendar.getInstance().toTimeWake(alarmDao.placePicked!!.arriveTime)
        time.currentHour = currentTime.hourOfDay
        time.currentMinute = currentTime.minute

        startPlace = alarmDao.placePicked!!.departurePlace
        destPlace = alarmDao.placePicked!!.arrivalPlace

        if (alarmDao.repeatDay.size != 0) {
            cbRepeat.isChecked = true
            repeatDayList = alarmDao.repeatDay
            repeatDayViewGroup.setCheckedDay(alarmDao.repeatDay)
        } else {
            cbRepeat.isChecked = false
        }

        toggleRepeatDayVisible()

        tvSetStart.text = alarmDao.placePicked?.departurePlace?.name
        tvSetDestination.text = alarmDao.placePicked?.arrivalPlace?.name
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

    private fun isPickLocation(): Boolean {
        // TODO : Check only arrival location
        return destPlace != null
    }

    private fun getCalculateDurationInTrafficObservable(): Observable<NetworkModel.ElementValue> {
        val startLatLng = "${startPlace!!.latLng.latitude},${startPlace!!.latLng.longitude}"
        val destLatLng = "${destPlace!!.latLng.latitude},${destPlace!!.latLng.longitude}"
        val arrivalTime = WaketimeUtil.decideWakeupTimeMillis(Model.AlarmDao(currentDate, currentTime, listOf()))
        val travelInfoArrivalObservable = HttpManager.getTravelDurationArrival(startLatLng, destLatLng, arrivalTime.toString())

        log("$startLatLng / $destLatLng -> $arrivalTime ")

        return travelInfoArrivalObservable
                .doOnNext { if (it.rows.size == 0) throw ArrayIndexOutOfBoundsException(it.errorMessage) }
                .flatMap { HttpManager.getTravelDurationDeparture(startLatLng, destLatLng, calculateApproxDepartureTime(arrivalTime, it.rows[0].elements[0].duration.value * 1000).toString()) }
                .flatMap {
                    Observable.just(it.rows[0].elements[0].durationInTraffic)
                }
    }

    private fun calculateRealDepartureTime(durationInTraffic: Long, arrivalTime: Long): Long {
        return arrivalTime - durationInTraffic - PERSONAL_TIME_OFFSET
    }

    private fun calculateApproxDepartureTime(arrivalTime: Long, duration: Long): Long {
        return arrivalTime - duration
    }

    private fun getLocationSettingObservable(): Observable<Boolean> {
        return locationProvider
                .checkLocationSettings(locationSetting)
                .doOnNext {
                    if (it.status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            it.status.startResolutionForResult(activity, CHECK_LOCATION_SETTING_REQUEST)
                        } catch(ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        }
                    }
                }
                .flatMap { locationSettingObservable }
    }

    private fun generateAlarmDao(durationInTraffic: NetworkModel.ElementValue): Model.AlarmDao? {
        log(durationInTraffic.toString())

        /* initialize alarmDao from current date and time */
        alarmDao = Model.AlarmDao(currentDate, currentTime, repeatDayList)

        /* calculate which time user should wakeup in millis*/
        val realDepartureTime: Long = calculateRealDepartureTime(durationInTraffic.value * 1000,
                alarmDao!!.toCalendar().timeInMillis)

        /* initialize model keep where is user destination and start */
        val placePicked = Model.PlacePicked(destPlace!!,
                startPlace!!,
                alarmDao!!.toCalendar().timeInMillis,
                durationInTraffic.value * 1000)

        /* set new alarmDao to the correct wakeup time, set repeat day list, and also placePicked */
        alarmDao = Model.AlarmDao(mCalendar().toDatePicked(realDepartureTime),
                mCalendar().toTimeWake(realDepartureTime),
                repeatDayList,
                placePicked)

        return alarmDao
    }

    /** Listener zone **/

    val cbRepeatListener = { view: View ->
        toggleRepeatDayVisible()
    }

    val btnSetAlarmListener = { view: View ->

        if (isPickLocation() || alarmDao?.placePicked != null) {

            val progressDialog = ProgressDialog.show(activity, "Please wait", "Calculating travel time..", true)

            isEnabledLocation = false
            if (startPlace == null) {
                travelInfoSubscription =
                        locationProvider
                            .checkLocationSettings(locationSetting)
                            .subscribe {
                                if (it.status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                    try {
                                        it.status.startResolutionForResult(activity, CHECK_LOCATION_SETTING_REQUEST)
                                    } catch(ex: IntentSender.SendIntentException) {
                                        ex.printStackTrace()
                                    }
                                } else {
                                    isEnabledLocation = true
                                    locationSettingObservable.onNext(isEnabledLocation)
                                }
                            }


               travelInfoSubscription = locationSettingObservable
                        .doOnNext { if(!it) progressDialog.dismiss() }
                        .filter { it }
                        .flatMap { locationProvider.getUpdatedLocation(locationRequest) }
                        .flatMap {
                            startPlace = Model.PlaceDetail("", "${it.latitude},${it.longitude}", Model.PlaceLatLng(it.latitude, it.longitude))
                            locationProvider.getReverseGeocodeObservable(it.latitude, it.longitude, 1)
                        }
                        .map {
                            if (it.size > 0 && it != null) {
                                startPlace?.name = it[0].featureName
                            }
                            it
                        }
                        .flatMap {
                            getCalculateDurationInTrafficObservable()
                        }
                        .map { generateAlarmDao(it) }
                        .doOnTerminate { progressDialog.dismiss() }
                        .subscribe ({ it ->
                            progressDialog.dismiss()
                            //                            tvSetStart.text = startPlace?.name
                            if (editIndex == -1) {
                                setAlarm(it!!)
                            } else {
                                updateAlarm(it!!, editIndex)
                            }
                        }, { error -> toast("Embarrassing, error has occurred -> ${error.message}") })


            } else {
                travelInfoSubscription = getCalculateDurationInTrafficObservable()
                        .map { generateAlarmDao(it) }
                        .doOnTerminate { progressDialog.dismiss() }
                        .subscribe ({ it ->
                            if (editIndex == -1) setAlarm(it!!)
                            else updateAlarm(it!!, editIndex)
                        }, { error -> toast("Embarrassing, error has occurred -> ${error.message}") })
            }


        } else {
            toast("Please pick arrival location")
        }
    }

    val timeChangedListener = { timePicker: TimePicker, hour: Int, min: Int ->
        currentTime = Model.TimeWake(hour, min)
        tvDestTime.text = "${getString(R.string.fragment_maps_alarm_destination)} ${currentTime.getTimeFormat()}"
    }

    val btnSetStartListener = { view: View ->

        // TODO: Check if location setting enabled
        startActivityForResult(builder.build(activity), START_PLACE_PICKER_REQUEST);

        //        startActivityForResult(Intent(activity, MapsActivity::class.java).putExtra(MapsActivity.EXTRA_TOOLBAR_TITLE, "Pick Start"),
        //                MapsActivity.REQUEST_CODE_START)
    }

    val btnSetDestListener = { view: View ->
        // TODO: Check if location setting enabled
        startActivityForResult(builder.build(activity), DESTINATION_PLACE_PICKER_REQUEST)
        //        startActivityForResult(Intent(activity, MapsActivity::class.java).putExtra(MapsActivity.EXTRA_TOOLBAR_TITLE, "Pick Destination"),
        //                MapsActivity.REQUEST_CODE_DEST)
    }
}