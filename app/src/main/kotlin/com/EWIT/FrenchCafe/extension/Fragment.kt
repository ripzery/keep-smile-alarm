package com.EWIT.FrenchCafe.extension

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.util.SharePref

/**
 * Created by Euro on 3/4/16 AD.
 */

fun Fragment.log(msg: String? = "Hello") {
    try {
        Log.d(activity.localClassName, msg)
    }catch(e: Exception){
        e.printStackTrace()
    }
}

fun Fragment.replaceFragment(fragmentContainer: Int = R.id.contentContainer, fragment: Fragment) {
    childFragmentManager.beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
}

fun Fragment.snackbar(rootView: View, msg: CharSequence = "Hi") {
    snackbar?.dismiss()
    snackbar = Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT);
    snackbar?.show()
}

fun Fragment.toast(msg: CharSequence = "Toast"){
    toast?.cancel()
    toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
    toast?.show()
}

var toast: Toast? = null
var snackbar: Snackbar? = null
var isSnackbarShown = false