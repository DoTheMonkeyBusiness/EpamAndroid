package com.example.epamandroid.views

import android.Manifest
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Size
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.camera_fragment.*
import android.widget.Toast
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.content.Context
import android.graphics.Bitmap
import android.view.*
import android.os.HandlerThread
import android.util.Log
import com.example.neuralnetwork.ImageClassifier
import java.io.IOException

class CameraFragment : Fragment() {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION: Int = 200
        private const val EMPTY_STRING_KEY: String = ""
        private const val THREAD_NAME_KEY: String = "Camera Background"
        private const val TAG: String = "Camera CameraFragment"
    }

    private var callback: IChangeFragmentCameraItemCallback? = null
    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null
    private var imageDimension: Size? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var cameraId: String = EMPTY_STRING_KEY
    private var runClassifier: Boolean = false

    private lateinit var imageClassifier: ImageClassifier

    private val lock: Any = Any()

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }

    }

    private val textureListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera()
        }
    }

    private val periodicClassify = object : Runnable {
        override fun run() {
            synchronized(lock) {
                if (runClassifier) {
                    classifyFrame()
                }
            }
            backgroundHandler?.post(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraFragmentTextureView.surfaceTextureListener = textureListener

        cameraFragmentBackToMenuButton.setOnClickListener {
            callback?.onItemChangedToMain()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            imageClassifier = ImageClassifier(activity)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize an image classifier.")
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IChangeFragmentCameraItemCallback) {
            callback = context
        }
    }

    override fun onDestroy() {
        imageClassifier.close()
        super.onDestroy()
    }

    private fun createCameraPreview() {
        try {
            val texture: SurfaceTexture = cameraFragmentTextureView.surfaceTexture
            imageDimension?.width?.let { imageDimension?.height?.let { it1 -> texture.setDefaultBufferSize(it, it1) } }
            val surface = Surface(texture)

            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(surface)

            cameraDevice?.createCaptureSession(
                    arrayListOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            Toast.makeText(context, getString(R.string.configure_failed), Toast.LENGTH_SHORT).show()
                        }

                        override fun onConfigured(session: CameraCaptureSession) {
                            cameraCaptureSessions = session
                            updatePreview()
                        }

                    },
                    null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(
                    context, getString(R.string.error), Toast.LENGTH_SHORT)
                    .show()
        }

        captureRequestBuilder
                ?.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)

        try {
            cameraCaptureSessions
                    ?.setRepeatingRequest(
                            captureRequestBuilder?.build(), null, backgroundHandler
                    )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera() {
        val manager: CameraManager = context
                ?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            imageDimension = map
                    ?.getOutputSizes(SurfaceTexture::class.java)?.get(0)

            if (context?.let {
                        ActivityCompat
                                .checkSelfPermission(
                                        it, Manifest.permission.CAMERA
                                )
                    }
                    != PackageManager.PERMISSION_GRANTED) {
                activity?.let {
                    ActivityCompat
                            .requestPermissions(
                                    it, arrayOf(
                                    Manifest
                                            .permission
                                            .CAMERA,
                                    Manifest
                                            .permission
                                            .WRITE_EXTERNAL_STORAGE),
                                    REQUEST_CAMERA_PERMISSION)
                }
                return
            }

            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION
                && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                    context, getString(R.string.permissions_error), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onResume() {
        super.onResume()

        startBackgroundThread()

        if (cameraFragmentTextureView.isAvailable) {
            openCamera()
        } else {
            cameraFragmentTextureView.surfaceTextureListener = textureListener
        }
    }

    override fun onPause() {
        super.onPause()

        stopBackgroundThread()
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()

        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
            synchronized(lock) {
                runClassifier = true
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread(THREAD_NAME_KEY)
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread?.looper)
        synchronized(lock) {
            runClassifier = true
        }
        backgroundHandler?.post(periodicClassify)
    }

    private fun classifyFrame() {
        if (activity == null || cameraDevice == null) {
            setBreedText(getString(R.string.uninitialized_classifier))
            return
        }

        val bitmap: Bitmap = cameraFragmentTextureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y)
        val textToShow = imageClassifier.classifyFrame(bitmap)
        bitmap.recycle()
        setBreedText(textToShow)
    }

    fun setBreedText(breed: String) {
        activity?.runOnUiThread {
            cameraFragmentDogBreed?.text = breed
        }
    }

    interface IChangeFragmentCameraItemCallback {
        fun onItemChangedToMain()
    }
}