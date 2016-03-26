package com.EWIT.FrenchCafe.manager

/**
 * Created by Euro on 3/26/16 AD.
 */

import android.annotation.TargetApi
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Bitmap
import android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.MediaRecorder.OutputFormat.MPEG_4
import android.media.MediaRecorder.VideoEncoder.H264
import android.media.MediaRecorder.VideoSource.SURFACE
import android.media.MediaScannerConnection
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.os.Environment.DIRECTORY_MOVIES
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

internal class RecordingManager(private val context: Context, private val listener: RecordingManager.Listener, private val resultCode: Int, private val data: Intent) {

    internal interface Listener {
        /** Invoked immediately prior to the start of recording.  */
        fun onStart()

        /** Invoked immediately after the end of recording.  */
        fun onStop()

        /** Invoked after all work for this session has completed.  */
        fun onEnd()
    }

    private val mainThread = Handler(Looper.getMainLooper())

    private val outputRoot: File
    private val fileFormat = SimpleDateFormat("'Telecine_'yyyy-MM-dd-HH-mm-ss'.mp4'", Locale.US)

    private val notificationManager: NotificationManager
    private val windowManager: WindowManager
    private val projectionManager: MediaProjectionManager

    private var recorder: MediaRecorder? = null
    private var projection: MediaProjection? = null
    private var display: VirtualDisplay? = null
    private var outputFile: String? = null
    private var running: Boolean = false
    private var recordingStartNanos: Long = 0

    init {
        val picturesDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES)
        outputRoot = File(picturesDir, "Telecine")

        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        projectionManager = context.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private // Get the best camera profile available. We assume MediaRecorder supports the highest.
    val recordingInfo: RecordingInfo
        get() {
            val displayMetrics = DisplayMetrics()
            val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getRealMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val displayHeight = displayMetrics.heightPixels
            val displayDensity = displayMetrics.densityDpi

            val configuration = context.resources.configuration
            val isLandscape = configuration.orientation == ORIENTATION_LANDSCAPE
            val camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
            val cameraWidth = if (camcorderProfile != null) camcorderProfile.videoFrameWidth else -1
            val cameraHeight = if (camcorderProfile != null) camcorderProfile.videoFrameHeight else -1
            val cameraFrameRate = if (camcorderProfile != null) camcorderProfile.videoFrameRate else 30


            return calculateRecordingInfo(displayWidth, displayHeight, displayDensity, isLandscape,
                    cameraWidth, cameraHeight, cameraFrameRate, 100)
        }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) fun startRecording() {

        // TODO : Build better recording algorithm :D

        if (!outputRoot.mkdirs()) {
            // We're probably about to crash, but at least the log will indicate as to why.
        }

        val recordingInfo = recordingInfo

        recorder = MediaRecorder()
        recorder!!.setVideoSource(SURFACE)
        recorder!!.setOutputFormat(MPEG_4)
        recorder!!.setVideoFrameRate(recordingInfo.frameRate)
        recorder!!.setVideoEncoder(H264)
        recorder!!.setVideoSize(recordingInfo.width, recordingInfo.height)
        recorder!!.setVideoEncodingBitRate(8 * 1000 * 1000)

        val outputName = fileFormat.format(Date())
        outputFile = File(outputRoot, outputName).absolutePath
        recorder!!.setOutputFile(outputFile)

        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            throw RuntimeException("Unable to prepare MediaRecorder.", e)
        }

        projection = projectionManager.getMediaProjection(resultCode, data)

        val surface = recorder!!.surface
        display = projection!!.createVirtualDisplay(DISPLAY_NAME, recordingInfo.width, recordingInfo.height,
                recordingInfo.density, VIRTUAL_DISPLAY_FLAG_PRESENTATION, surface, null, null)

        recorder!!.start()
        running = true
        recordingStartNanos = System.nanoTime()
        listener.onStart()

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) fun stopRecording() {

        if (!running) {
            throw IllegalStateException("Not running.")
        }
        running = false

        // Stop the projection in order to flush everything to the recorder.
        projection!!.stop()

        // Stop the recorder which writes the contents to the file.
        recorder!!.stop()

        val recordingStopNanos = System.nanoTime()

        recorder!!.release()
        display!!.release()


        listener.onStop()

        MediaScannerConnection.scanFile(context, arrayOf<String>(outputFile!!), null
        ) { path, uri -> mainThread.post { } }
    }

    internal class RecordingInfo(val width: Int, val height: Int, val frameRate: Int, val density: Int)

    fun destroy() {
        if (running) {
            stopRecording()
        }
    }

    fun isRunning(): Boolean{
        return running
    }

    companion object {

        private val DISPLAY_NAME = "telecine"
        private val MIME_TYPE = "video/mp4"

        fun calculateRecordingInfo(displayWidth: Int, displayHeight: Int,
                                   displayDensity: Int, isLandscapeDevice: Boolean, cameraWidth: Int, cameraHeight: Int,
                                   cameraFrameRate: Int, sizePercentage: Int): RecordingInfo {
            // Scale the display size before any maximum size calculations.
            var width = displayWidth * sizePercentage / 100
            var height = displayHeight * sizePercentage / 100

            if (cameraWidth == -1 && cameraHeight == -1) {
                // No cameras. Fall back to the display size.
                return RecordingInfo(width, height, cameraFrameRate, displayDensity)
            }

            var frameWidth = if (isLandscapeDevice) cameraWidth else cameraHeight
            var frameHeight = if (isLandscapeDevice) cameraHeight else cameraWidth
            if (frameWidth >= width && frameHeight >= height) {
                // Frame can hold the entire display. Use exact values.
                return RecordingInfo(width, height, cameraFrameRate, displayDensity)
            }

            // Calculate new width or height to preserve aspect ratio.
            if (isLandscapeDevice) {
                frameWidth = width * frameHeight / height
            } else {
                frameHeight = height * frameWidth / width
            }
            return RecordingInfo(frameWidth, frameHeight, cameraFrameRate, displayDensity)
        }

    }
}