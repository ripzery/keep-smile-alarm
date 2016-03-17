package com.EWIT.FrenchCafe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.fragment.MapsFragment

/**
 * Created by Euro on 3/10/16 AD.
 */

class MapsActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var mapsFragment: MapsFragment

    /** Lifecycle  zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initInstance()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }


    /** Method zone **/

    private fun initInstance() {
        mapsFragment = MapsFragment.newInstance("MapsFragment")
        replaceFragment(R.id.contentContainer, mapsFragment)
    }

    /** Listener zone **/

}