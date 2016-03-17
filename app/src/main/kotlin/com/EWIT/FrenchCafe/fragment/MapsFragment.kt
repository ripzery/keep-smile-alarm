package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.activity.MapsActivity
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.extension.toast
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_maps.*
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Subscription

/**
 * Created by Euro on 3/10/16 AD.
 */
class MapsFragment : Fragment(), OnMapReadyCallback {
    /** Variable zone **/
    var isDest: Boolean = false
    lateinit private var currentLocation: Location
    lateinit private var supportMapsFragment: SupportMapFragment
    lateinit private var locationProvider: ReactiveLocationProvider
    lateinit private var locationRequestSubscription: Subscription
    lateinit private var lastKnownLocationSubscription: Subscription
    lateinit private var mMap: GoogleMap
    private val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(3)
            .setInterval(1000);

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"

        fun newInstance(param1: Boolean): MapsFragment {
            var bundle: Bundle = Bundle()
            bundle.putBoolean(ARG_1, param1)
            val mapsFragment: MapsFragment = MapsFragment()
            mapsFragment.arguments = bundle
            return mapsFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            isDest = arguments.getBoolean(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_maps, container, false)

        return rootView
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        lastKnownLocationSubscription.unsubscribe()
        locationRequestSubscription.unsubscribe()
    }

    /** Override method zone **/

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        lastKnownLocationSubscription = ReactiveLocationProvider(activity).lastKnownLocation.subscribe { moveToLocation(map, it) }
        locationRequestSubscription = locationProvider.getUpdatedLocation(locationRequest).subscribe { moveToLocation(map, it) }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance() {

        supportMapsFragment = SupportMapFragment.newInstance()
        replaceFragment(R.id.mapContainer, supportMapsFragment)
        supportMapsFragment.getMapAsync(this)
        locationProvider = ReactiveLocationProvider(activity)

        locationProvider.checkLocationSettings(LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true).build())
                .subscribe {
                    val status = it.status
                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(activity, 1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

        btnStartLocation.visibility = if (isDest) View.GONE else View.VISIBLE
        btnDestLocation.visibility = if (isDest) View.VISIBLE else View.GONE

        ivPickLocation.setImageDrawable(ContextCompat.getDrawable(activity, if(isDest) R.drawable.ic_place_teal_24dp else R.drawable.ic_place_red_24dp ))

        btnStartLocation.setOnClickListener(onPickLocationListener)
        btnDestLocation.setOnClickListener(onPickLocationListener)
    }

    fun userNotEnabledLocation() {
        toast("Please enabled location setting first")
        activity.finish()
    }

    fun userEnabledLocation() {
        log("User enabled location")
        locationRequestSubscription = locationProvider.getUpdatedLocation(locationRequest).subscribe { moveToLocation(mMap, it) }

    }

    private fun moveToLocation(map: GoogleMap?, location: Location) {
        currentLocation = location

        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(with(location) { LatLng(latitude, longitude) }, 17.0f), object : GoogleMap.CancelableCallback{
            override fun onCancel() {

            }

            override fun onFinish() {
                btnStartLocation.isEnabled = true
                btnDestLocation.isEnabled = true
            }

        })
    }

    /** Listener zone **/

    var onPickLocationListener = { view: View ->
        val intent = Intent()
        intent.putExtra(MapsActivity.RETURN_INTENT_EXTRA_LATLNG, "${currentLocation.latitude}, ${currentLocation.longitude}")
        log(currentLocation.toString())
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }
}