package com.EWIT.FrenchCafé.extension

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.EWIT.FrenchCafé.R
import com.EWIT.FrenchCafé.util.SharePref

/**
 * Created by Euro on 3/3/16 AD.
 */

fun AppCompatActivity.log(text: String = "Hello"){
    Log.d(this.localClassName, text)
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