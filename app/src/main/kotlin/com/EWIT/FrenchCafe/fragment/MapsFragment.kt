package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.app.ProgressDialog
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
import rx.Observable
import rx.Subscription

/**
 * Created by Euro on 3/10/16 AD.
 */
class MapsFragment : Fragment(), OnMapReadyCallback {
    /** Variable zone **/
    var isDest: Boolean = false
    lateinit private var supportMapsFragment: SupportMapFragment
    lateinit private var locationProvider: ReactiveLocationProvider
    lateinit private var locationRequestSubscription: Subscription
    lateinit private var lastKnownLocationSubscription: Subscription
    lateinit private var mMap: GoogleMap
    lateinit private var progressDialog: ProgressDialog
    private val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(1)
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
        try {
            lastKnownLocationSubscription.unsubscribe()
            locationRequestSubscription.unsubscribe()
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    /** Override method zone **/

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        mMap.isMyLocationEnabled = true
        startSubscribeUserLastKnownLocation()
        startSubscribeUserLocation()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    fun userNotEnabledLocation() {
        toast("Please enabled location setting first")
        activity.finish()
    }

    fun userEnabledLocation() {
        log("User enabled location")
        startSubscribeUserLocation()
    }

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

        ivPickLocation.setImageDrawable(ContextCompat.getDrawable(activity, if (isDest) R.drawable.ic_place_teal_24dp else R.drawable.ic_place_red_24dp))

        btnStartLocation.setOnClickListener(onPickLocationListener)
        btnDestLocation.setOnClickListener(onPickLocationListener)
    }

    private fun startSubscribeUserLocation() {
        locationRequestSubscription = locationProvider.getUpdatedLocation(locationRequest)
                .subscribe {
                    moveToLocation(mMap, it)
                }
    }

    private fun startSubscribeUserLastKnownLocation() {
        lastKnownLocationSubscription = ReactiveLocationProvider(activity).lastKnownLocation
            .subscribe {
                moveToLocation(mMap, it)
            }
    }

    private fun animateToLocation(map: GoogleMap?, location: Location) {
        enablePickLocationButton(false)

        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(with(location) { LatLng(latitude, longitude) }, 17.0f), object : GoogleMap.CancelableCallback {
            override fun onCancel() {
                enablePickLocationButton(true)
            }

            override fun onFinish() {
                enablePickLocationButton(true)
            }

        })
    }

    private fun moveToLocation(map: GoogleMap?, location: Location){
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(with(location) { LatLng(latitude, longitude) }, 17.0f))
        enablePickLocationButton(true)

    }

    private fun enablePickLocationButton(isEnable: Boolean){
        btnStartLocation.isEnabled = isEnable
        btnDestLocation.isEnabled = isEnable
    }

    private fun getCenterLatLngPlaceObservable(): Observable<Pair<LatLng, String>> {
        val centerLatLng = mMap.cameraPosition.target

        return locationProvider.getReverseGeocodeObservable(centerLatLng.latitude, centerLatLng.longitude, 1)
                .map { "${it[0].getAddressLine(0)} ${it[0].getAddressLine(1)}" }
                .map { centerLatLng to it }
    }

    /** Listener zone **/

    var onPickLocationListener = { view: View ->

        /* don't remove val */
        progressDialog = ProgressDialog.show(activity, "Please wait", "Loading place name...", true)

        val subscription = getCenterLatLngPlaceObservable()
            .subscribe {
                progressDialog.dismiss()
                var (latLng, place) = it
                val intent = Intent()
                intent.putExtra(MapsActivity.RETURN_INTENT_EXTRA_LATLNG, "${latLng.latitude}, ${latLng.longitude}")
                intent.putExtra(MapsActivity.RETURN_INTENT_EXTRA_PLACE, place)
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
    }
}