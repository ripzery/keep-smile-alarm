package com.EWIT.FrenchCafe.fragment

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.google.android.gms.vision.face.Face
import kotlinx.android.synthetic.main.fragment_wake_tracker.*


class WakeTrackerFragment : Fragment() {

    /** Variable zone **/

    private val STATE_LEFT_SCREEN = 0;
    private val STATE_CLOSE_EYE = 1;
    private val STATE_OPEN_EYE = 2;
    private val STATE_COMPLETE = 3;
    private var rootView: View? = null
    private val EYE_OPEN_DURATION = 15000L
    private var isCounterRunning = false;
    private var isBreakMission = false
    var isCompleted: Boolean = false
    private var alarmSound: String? = null
    private var ringtone: MediaPlayer? = null
    private var backgroundFaceDetectionFragment: BackgroundFaceDetectionFragment? = null


    /*** Static object zone  ***/

    companion object {

        val ALARM_SOUND = "ARG_1"

        fun getInstance(alarmSoundUriString: String? = null): WakeTrackerFragment {
            var bundle: Bundle = Bundle()

            bundle.putString(ALARM_SOUND, alarmSoundUriString)

            var wakeTrackerFragment: WakeTrackerFragment = WakeTrackerFragment()

            wakeTrackerFragment.arguments = bundle

            return wakeTrackerFragment

        }
    }

    /**** Lifecycle Zone ****/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            alarmSound = arguments.getString(ALARM_SOUND)
        }
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
        stopAlarm()
    }

    override fun onResume() {
        super.onResume()
        backgroundFaceDetectionFragment?.setFaceTrackerListener(backgroundFaceDetectionListener)
        if (!isCompleted) playAlarm()

    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.release()
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
        backgroundFaceDetectionFragment!!.setFaceTrackerListener(backgroundFaceDetectionListener)

        replaceFragment(R.id.cameraContainer, backgroundFaceDetectionFragment!!)

        playAlarm()
    }

    private fun playAlarm() {
        try {
            val uri: Uri
            if (alarmSound != null) {
                uri = Uri.parse(alarmSound)
            } else {
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            }
            if(ringtone == null) {
                ringtone = MediaPlayer()
                ringtone!!.reset()
                ringtone!!.setDataSource(context, uri)
                ringtone!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                ringtone!!.isLooping = true
            }

            if(!(ringtone as MediaPlayer).isPlaying) {
                ringtone!!.prepare()
                ringtone!!.start()
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopAlarm() {
        ringtone?.stop()
    }

    /**** Listener zone ****/

    private var countDownTimer = object : CountDownTimer(EYE_OPEN_DURATION, 1000) {
        override fun onFinish() {
            val text = "Congratulation!"
            ivWakeState.setImageLevel(STATE_COMPLETE)
            backgroundFaceDetectionFragment?.removeFaceTrackerListener()
            tvHello.text = text
            isCompleted = true
            stopAlarm()
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
            activity.runOnUiThread { //                tvHello.text = "Open your eye"
                //                ivWakeState.setImageLevel(STATE_CLOSE_EYE)
                //                showSnackbar(contentContainer, "Open your eye")
            }
        }
    }

}
