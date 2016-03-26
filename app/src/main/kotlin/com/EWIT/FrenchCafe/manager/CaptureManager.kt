package com.EWIT.FrenchCafe.manager

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build

import android.content.Context.MEDIA_PROJECTION_SERVICE

internal class CaptureManager private constructor() {

    init {
        throw AssertionError("No instances.")
    }

    companion object {
        private val CREATE_SCREEN_CAPTURE = 4242

        @TargetApi(Build.VERSION_CODES.LOLLIPOP) fun fireScreenCaptureIntent(activity: Activity) {
            val manager = activity.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            val intent = manager.createScreenCaptureIntent()
            activity.startActivityForResult(intent, CREATE_SCREEN_CAPTURE)

        }

        fun handleActivityResult(activity: Activity, requestCode: Int, resultCode: Int,
                                 data: Intent): Intent? {
            if (requestCode != CREATE_SCREEN_CAPTURE) {
                return null
            }

            if (resultCode == Activity.RESULT_OK) {
                return data
                //activity.startService(TelecineService.newIntent(activity, resultCode, data));
            }
            return null
        }
    }
}
