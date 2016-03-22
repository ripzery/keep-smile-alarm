package com.EWIT.FrenchCafe.extension

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.util.SharePref

/**
 * Created by Euro on 3/3/16 AD.
 */

fun AppCompatActivity.log(text: String = "Hello"){
    Log.d(this.localClassName, text)
}

fun AppCompatActivity.toast(msg: CharSequence = "Toast"){
    toast?.cancel()
    toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast?.show()
}

fun AppCompatActivity.replaceFragment(fragmentContainer: Int = R.id.contentContainer, fragment: Fragment){
    supportFragmentManager.beginTransaction()
    .replace(fragmentContainer, fragment)
    .commit()
}

fun AppCompatActivity.addFragment(fragmentContainer: Int = R.id.contentContainer, fragment: Fragment){
    supportFragmentManager.beginTransaction()
            .add(fragmentContainer, fragment)
            .commit()
}