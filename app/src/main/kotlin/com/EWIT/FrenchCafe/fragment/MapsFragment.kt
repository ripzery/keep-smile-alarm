package com.EWIT.FrenchCafe.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by Euro on 3/10/16 AD.
 */
class MapsFragment : Fragment(), OnMapReadyCallback{
    /** Variable zone **/
    lateinit var param1: String
    lateinit var supportMapsFragment: SupportMapFragment


    /** Static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : MapsFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val mapsFragment: MapsFragment = MapsFragment()
            mapsFragment.arguments = bundle
            return mapsFragment
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
        val rootView: View = inflater!!.inflate(R.layout.fragment_maps, container, false)

        return rootView
    }

    /** Override method zone **/

    override fun onMapReady(map: GoogleMap?) {
        map!!.addMarker(MarkerOptions()
                .position(LatLng(0.02, 0.0))
                .title("Marker"));
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supportMapsFragment = SupportMapFragment.newInstance()
        replaceFragment(R.id.mapContainer, supportMapsFragment)
        supportMapsFragment.getMapAsync(this)
    }

    /** Method zone **/

    private fun initInstance(){

    }
}