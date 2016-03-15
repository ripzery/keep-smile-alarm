package com.EWIT.FrenchCafé.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.EWIT.FrenchCafé.R
import com.EWIT.FrenchCafé.lib.viewgroup.GraphicOverlay
import com.EWIT.FrenchCafé.viewgroup.FaceGraphic
import kotlinx.android.synthetic.main.fragment_face_preview.*
import java.io.IOException

/**
 * Created by Euro on 3/3/16 AD.
 */


class BackgroundFaceDetectionFragment() : Fragment() {

    /** Variable zone **/

    private var mCameraSource: CameraSource? = null
    private val TAG: String? = "FaceTrackerFragment"
    private var eyeThresholdSensitivity: Float = 0.5F
    private var faceTrackerListener: FaceTrackerListener? = null

    /** Static method zone **/

    companion object {
        val ARG1 = "EYE_THRESHOLD_SENSITIVITY"
        private val RC_HANDLE_GMS = 9001
        // permission request codes need to be < 256
        private val RC_HANDLE_CAMERA_PERM = 2

        fun getInstance(eyeThresholdSensitivity: Float? = 0.5F): BackgroundFaceDetectionFragment {
            var bundle: Bundle = Bundle()

            bundle.putFloat(ARG1, eyeThresholdSensitivity!!)

            var backgroundFaceDetectionFragment: BackgroundFaceDetectionFragment = BackgroundFaceDetectionFragment()
            backgroundFaceDetectionFragment.arguments = bundle
            return backgroundFaceDetectionFragment
        }
    }

    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eyeThresholdSensitivity = arguments.getFloat(ARG1, 0.5F)
        Log.d("", "$eyeThresholdSensitivity")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_face_preview, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        val rc = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }

    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private fun requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission")

        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        val thisActivity = this

        val listener = View.OnClickListener { ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERM) }

        //Snackbar.make(mGraphicOverlay, com.google.android.gms.samples.vision.face.facetracker.R.string.permission_camera_rationale,
        //        Snackbar.LENGTH_INDEFINITE)
        //        .setAction(com.google.android.gms.samples.vision.face.facetracker.R.string.ok, listener)
        //        .show();
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()

        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        preview.stop()
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mCameraSource != null) {
            mCameraSource!!.release()
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *

     * @param requestCode The request code passed in [.requestPermissions].
     * *
     * @param permissions The requested permissions. Never null.
     * *
     * @param grantResults The grant results for the corresponding permissions
     * * which is either [PackageManager.PERMISSION_GRANTED]
     * * or [PackageManager.PERMISSION_DENIED]. Never null.
     * *
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source")
            // we have permission, so create the camerasource
            createCameraSource()
            return
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.size +
                " Result code = " + if (grantResults.size > 0) grantResults[0] else "(empty)")

        //        val listener = DialogInterface.OnClickListener { dialog, id -> finish() }

        //        val builder = AlertDialog.Builder(this)
        //builder.setTitle("Face Tracker sample")
        //        .setMessage(com.google.android.gms.samples.vision.face.facetracker.R.string.no_camera_permission)
        //        .setPositiveButton(com.google.android.gms.samples.vision.face.facetracker.R.string.ok, listener)
        //        .show();
    }

    /** Method zone **/

    fun setFaceTrackerListener(faceTrackerListener: FaceTrackerListener) {
        this@BackgroundFaceDetectionFragment.faceTrackerListener = faceTrackerListener
    }

    fun removeFaceTrackerListener() {
        this@BackgroundFaceDetectionFragment.faceTrackerListener = null
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private fun createCameraSource() {

        val context = activity.applicationContext
        val detector = FaceDetector.Builder(context).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).setLandmarkType(FaceDetector.ALL_LANDMARKS).build()

        detector.setProcessor(MultiProcessor.Builder<Face>(GraphicFaceTrackerFactory()).build())

        if (!detector.isOperational) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.")
        }

        mCameraSource = CameraSource.Builder(context, detector).setRequestedPreviewSize(640, 480).setFacing(CameraSource.CAMERA_FACING_FRONT).setRequestedFps(30.0f).build()
    }



    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {

        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity.applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(activity, code, RC_HANDLE_GMS)
            dlg.show()
        }

        if (mCameraSource != null) {
            try {
                preview.start(mCameraSource, faceOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                mCameraSource!!.release()
                mCameraSource = null
            }

        }
    }

    /** Listener zone **/
    interface FaceTrackerListener {
        fun onOpenEye(face: Face)
        fun onCloseEye(face: Face)
        fun onLeftCamera(face: Face)
    }


    /** Inner class zone **/
    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private inner class GraphicFaceTrackerFactory : MultiProcessor.Factory<Face> {
        override fun create(face: Face): Tracker<Face> {
            return GraphicFaceTracker(faceOverlay)
            //return new GraphicFaceTracker(null);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */


    inner class GraphicFaceTracker internal constructor(private val mOverlay: GraphicOverlay) : Tracker<Face>() {
        private val mFaceGraphic: FaceGraphic

        init {
            mFaceGraphic = FaceGraphic(mOverlay)
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        override fun onNewItem(faceId: Int, item: Face?) {
            mFaceGraphic.setId(faceId)
        }

        override fun onUpdate(detections: Detector.Detections<Face>?, item: Face?) {
            mOverlay.add(mFaceGraphic)
            mFaceGraphic.updateFace(item!!)
            if (item!!.isLeftEyeOpenProbability == -1.toFloat()) {
                faceTrackerListener?.onLeftCamera(item)
            } else if (item.isLeftEyeOpenProbability < eyeThresholdSensitivity) {
                faceTrackerListener?.onCloseEye(item)
            } else {
                faceTrackerListener?.onOpenEye(item)
            }
        }

        override fun onMissing(detections: Detector.Detections<Face>?) {
            mOverlay.remove(mFaceGraphic)
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        override fun onDone() {
            mOverlay.remove(mFaceGraphic)
        }
    }
}