package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.vision.face.Face
import com.socket9.eyealarm.fragment.BackgroundFaceDetectionFragment
import com.socket9.eyealarm.R
import com.socket9.eyealarm.extension.log
import com.socket9.eyealarm.extension.replaceFragment
import kotlinx.android.synthetic.main.fragment_wake_tracker.*


class WakeTrackerFragment : Fragment() {

    private val STATE_LEFT_SCREEN = 0;
    private val STATE_CLOSE_EYE = 1;
    private val STATE_OPEN_EYE = 2;
    private val STATE_COMPLETE = 3;
    private var rootView: View? = null
    private val EYE_OPEN_DURATION = 15000L
    private var isCounterRunning = false;
    private var isBreakMission = false
    private var backgroundFaceDetectionFragment: BackgroundFaceDetectionFragment? = null


    /*** Static object zone  ***/

    companion object {

        val ARG1 = "ARG_1"

        fun getInstance(param1: String? = null): WakeTrackerFragment {
            var bundle: Bundle = Bundle()

            bundle.putString(ARG1, param1)

            var wakeTrackerFragment: WakeTrackerFragment = WakeTrackerFragment()

            wakeTrackerFragment.arguments = bundle

            return wakeTrackerFragment

        }
    }

    /**** Lifecycle Zone ****/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_wake_tracker, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initInstance()

    }

    override fun onPause() {
        super.onPause()
        stopCounter()
        backgroundFaceDetectionFragment?.removeFaceTrackerListener()
    }

    override fun onResume() {
        super.onResume()
        backgroundFaceDetectionFragment?.setFaceTrackerListener(backgroundFaceDetectionListener)

    }

    /**** Method zone ****/

    private fun startCounter() {
        if (!isCounterRunning) {
            countDownTimer.start()
            isCounterRunning = true
        }
    }

    private fun restartCounter() {
        countDownTimer.cancel()
        countDownTimer.start()
        isCounterRunning = true
    }

    private fun stopCounter() {
        countDownTimer?.cancel()
        isCounterRunning = false
    }

    private fun initInstance() {
        backgroundFaceDetectionFragment = BackgroundFaceDetectionFragment.getInstance(0.4F)
        backgroundFaceDetectionFragment!!.setFaceTrackerListener(backgroundFaceDetectionListener!!)

        replaceFragment(R.id.cameraContainer, backgroundFaceDetectionFragment!!)
    }

    /**** Listener zone ****/

    private var countDownTimer = object : CountDownTimer(EYE_OPEN_DURATION, 1000) {
        override fun onFinish() {
            val text = "Congratulation!"
            ivWakeState.setImageLevel(STATE_COMPLETE)
            backgroundFaceDetectionFragment?.removeFaceTrackerListener()
            tvHello.text = text
        }

        override fun onTick(millisUntilFinished: Long) {
            if (!isBreakMission ) {
                tvHello.text = "${ Math.round(millisUntilFinished.toDouble()) / 1000}"
            } else {
                restartCounter()
                isBreakMission = false
            }
        }
    }

    private var backgroundFaceDetectionListener = object : BackgroundFaceDetectionFragment.FaceTrackerListener {
        override fun onLeftCamera(face: Face) {
            stopCounter()
            activity.runOnUiThread {
                tvHello.text = "Stay in Camera"
                ivWakeState.setImageLevel(STATE_LEFT_SCREEN)
            }
            isBreakMission = false
        }

        override fun onOpenEye(face: Face) {
            log("open : ${face.isLeftEyeOpenProbability}")
            startCounter()
            activity.runOnUiThread {
                ivWakeState.setImageLevel(STATE_OPEN_EYE)
            }
        }

        override fun onCloseEye(face: Face) {
            log("close : ${face.isLeftEyeOpenProbability}")

            isBreakMission = true
            activity.runOnUiThread {
                //                tvHello.text = "Open your eye"
                //                ivWakeState.setImageLevel(STATE_CLOSE_EYE)
                //                showSnackbar(contentContainer, "Open your eye")
            }
        }
    }

}
