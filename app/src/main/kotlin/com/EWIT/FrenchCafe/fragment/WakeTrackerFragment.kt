package com.EWIT.FrenchCafe.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.RingtoneManager
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.extension.currentMillis
import com.EWIT.FrenchCafe.extension.log
import com.EWIT.FrenchCafe.extension.replaceFragment
import com.EWIT.FrenchCafe.extension.supportsLollipop
import com.EWIT.FrenchCafe.manager.RecordingManager
import com.google.android.gms.vision.face.Face
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_wake_tracker.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class WakeTrackerFragment : Fragment() {

    /** Variable zone **/

    private val STATE_LEFT_SCREEN = 0;
    private val STATE_CLOSE_EYE = 1;
    private val STATE_OPEN_EYE = 2;
    private val STATE_COMPLETE = 3;
    private var rootView: View? = null
    private val EYE_OPEN_DURATION = 10000L
    private var isCounterRunning = false;
    private var isBreakMission = false
    var isCompleted: Boolean = false
    private var alarmSound: String? = null
    private var ringtone: MediaPlayer? = null
    private var backgroundFaceDetectionFragment: BackgroundFaceDetectionFragment? = null
    lateinit private var recorder: MediaRecorder
    lateinit private var mProjectionManager: MediaProjectionManager
    private var recordingManager: RecordingManager? = null
    private var projectionIntent: Intent? = null
    private var lastVideoUri: Uri? = null
    private var sharingChooser: Intent? = null

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
        if (recordingManager != null) stopRecord()
    }

    override fun onResume() {
        super.onResume()
        if (!isCompleted) {
            backgroundFaceDetectionFragment?.setFaceTrackerListener(backgroundFaceDetectionListener)
            playAlarm()
            if (projectionIntent != null) {
                startRecord()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.release()
    }

    /**** Method zone ****/

    private fun initInstance() {
        backgroundFaceDetectionFragment = BackgroundFaceDetectionFragment.getInstance(0.4F)
        backgroundFaceDetectionFragment!!.setFaceTrackerListener(backgroundFaceDetectionListener)
        replaceFragment(R.id.cameraContainer, backgroundFaceDetectionFragment!!)
        RxView.clicks(btnShare)
                .doOnNext { btnShare.isEnabled = false }
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    showSharingDialog(recordingManager?.getLatestOutputFile())
                    btnShare.isEnabled = true
                }
    }

    fun wakeWithRecording(intentData: Intent?) {
        projectionIntent = intentData
        recordingManager = RecordingManager(activity, object : RecordingManager.Listener {
            override fun onStop() {
                log("Stop")
            }

            override fun onEnd() {
                log("End")
            }

            override fun onStart() {
                log("Start")
            }

        }, Activity.RESULT_OK, projectionIntent!!)
        playAlarm()
        startRecord()
    }

    fun wakeWithoutRecording() {
        playAlarm()
    }

    private fun startCounter() {
        if (!isCounterRunning) {
            countDownTimer.start()
            isCounterRunning = true
        }
    }

    private fun stopCounter() {
        countDownTimer.cancel()
        isCounterRunning = false
    }

    private fun restartCounter() {
        stopCounter()
        startCounter()
    }

    private fun playAlarm() {
        try {
            val uri: Uri
            if (alarmSound != null) {
                uri = Uri.parse(alarmSound)
            } else {
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            }

            if (ringtone == null) {
                ringtone = MediaPlayer()
                ringtone!!.reset()
                ringtone!!.setDataSource(context, uri)
                ringtone!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                ringtone!!.isLooping = true
            }

            if (!(ringtone as MediaPlayer).isPlaying) {
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

    private fun startRecord() {
        if (!recordingManager!!.isRunning()) recordingManager?.startRecording()
    }

    private fun stopRecord() {
        recordingManager?.destroy()
    }

    private fun buildSharingDialog(latestOutputFile: String?) {
        if (sharingChooser == null) {
            log(latestOutputFile)
            val content: ContentValues = ContentValues(4)
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED, currentMillis() / 1000)
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            content.put(MediaStore.Video.Media.DATA, latestOutputFile)
            if (content == null) {
                log("Null content")
            }
            val resolver: ContentResolver = context.contentResolver
            try {
                val uri: Uri = resolver.insert(Uri.parse("content://media/external/video/media"), content)
                val sharingIntent: Intent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "video/*"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FrenchAlarm")
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri)
                sharingChooser = Intent.createChooser(sharingIntent, "Share video to..")
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showSharingDialog(latestOutputFile: String?) {
        startActivity(sharingChooser)
    }

    private fun showVideoPreview(latestOutputFile: String?) {
        childFragmentManager.beginTransaction().remove(backgroundFaceDetectionFragment).commit()
        contentContainer.visibility = View.GONE
        cameraContainer.visibility = View.GONE
        videoContainer.visibility = View.VISIBLE
        videoView.setVideoURI(Uri.parse(latestOutputFile))
        val mediaController = MediaController(activity)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(null)
        videoView.setOnClickListener(null)
        videoView.setOnCompletionListener { videoView.start() }
        videoView.start()

    }

    /**** Listener zone ****/

    private var countDownTimer = object : CountDownTimer(EYE_OPEN_DURATION, 500) {
        override fun onFinish() {
            if (!isCompleted) {
                log("$isCompleted")
                isCompleted = true
                tvHello.text = "Congratulation!"
                backgroundFaceDetectionFragment?.removeFaceTrackerListener()
                RxTextView.textChangeEvents(tvHello)
                        .doOnNext {
                            log("Test ${it.text().toString()}")
                            ivWakeState.setImageLevel(STATE_COMPLETE)
                        }
                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            stopAlarm()
                            stopRecord()
                            log("show dialog")
                            supportsLollipop { // TODO :Implemented play video preview and share button
                                buildSharingDialog(recordingManager?.getLatestOutputFile())
                                showVideoPreview(recordingManager?.getLatestOutputFile())
                            }
                        }
            }
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
