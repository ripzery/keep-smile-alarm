package com.socket9.eyealarm.extension

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.socket9.eyealarm.R

/**
 * Created by Euro on 3/4/16 AD.
 */

fun Fragment.log(msg: String? = "Hello") {
    Log.d(activity.localClassName, msg)
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