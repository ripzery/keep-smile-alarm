package com.EWIT.FrenchCafe.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.fragment.MapsFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Euro on 3/10/16 AD.
 */

class MapsActivity : AppCompatActivity(){

    /** Variable zone **/
    lateinit private var mapsFragment: MapsFragment
    lateinit private var toolbarTitle: String


    /** Static variable **/
    companion object{
        val EXTRA_TOOLBAR_TITLE = "title"
        val REQUEST_CODE_START = 1
        val REQUEST_CODE_DEST = 2
        val RETURN_INTENT_EXTRA_LATLNG = "latLng"
        val RETURN_INTENT_EXTRA_PLACE = "place"
    }

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

        if(item?.itemId == android.R.id.home){
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                mapsFragment.userEnabledLocation()
            }else{
                mapsFragment.userNotEnabledLocation()
            }
        }
    }


    /** Method zone **/

    private fun initInstance() {
        initToolbar()
        mapsFragment = MapsFragment.newInstance(toolbarTitle.equals("Pick Destination"))
        replaceFragment(R.id.contentContainer, mapsFragment)
    }


    private fun initToolbar() {
        toolbarTitle = intent.getStringExtra(EXTRA_TOOLBAR_TITLE)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = toolbarTitle
    }

    /** Listener zone **/

}