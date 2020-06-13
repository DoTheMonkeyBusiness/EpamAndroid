package com.example.epamandroid.mvp.views.fragments

import android.Manifest
import android.graphics.SurfaceTexture
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
import android.graphics.Matrix
import android.graphics.RectF
import android.hardware.camera2.*
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.util.Log
import com.example.epamandroid.constants.PermissionsConstants.REQUEST_CAMERA_PERMISSION_EXTRA_KEY
import com.example.epamandroid.mvp.contracts.ICameraContract
import com.example.epamandroid.mvp.presenters.CameraPresenter
import com.example.neuralnetwork.ImageClassifier
import java.io.IOException

class CameraFragment : Fragment(), ICameraContract.View {

    companion object {
        private const val TAG: String = "CameraFragment"
        private const val ZERO_FLOAT_KEY: Float = 0F
        private const val EMPTY_STRING_KEY: String = ""
    }

    private var changeFragmentCallback: IChangeFragmentCameraItemCallback? = null
    private var showBottomSheetCallback: IShowBottomSheetCallback? = null
    private var backgroundHandler: Handler? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var cameraId: String = EMPTY_STRING_KEY
    private var imageDimension: Size? = null
    private var isFragmentVisible: Boolean = false

    private lateinit var mainActivity: AppCompatActivity
    private lateinit var imageClassifier: ImageClassifier

    private val cameraPresenter: ICameraContract.Presenter = CameraPresenter(this)

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            closeCamera()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            closeCamera()
        }

    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(
                session: CameraCaptureSession,
                request: CaptureRequest,
                partialResult: CaptureResult
        ) = Unit

        override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
        ) = Unit
    }

    private val textureListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            transformImage(width.toFloat(), height.toFloat())
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) = Unit

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = (activity as AppCompatActivity)

        cameraFragmentBackToMenuButton.setOnClickListener {
            changeFragmentCallback?.onItemChangedToMain()
        }

        cameraFragmentRestaurantType.setOnClickListener {
            if (cameraFragmentRestaurantType.text != getString(R.string.uninitialized_classifier)) {
                showBottomSheetCallback
                        ?.onShowBottomSheetFromCamera(
                                cameraFragmentRestaurantType
                                        .text
                                        .toString()
                        )
            }
        }

        try {
            imageClassifier = ImageClassifier(activity)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize an image classifier.")
        }

        cameraFragmentTextureView.isOpaque = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IChangeFragmentCameraItemCallback) {
            changeFragmentCallback = context
        }
        if (context is IShowBottomSheetCallback) {
            showBottomSheetCallback = context
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (cameraFragmentTextureView.isAvailable) {
                openCamera()
            } else {
                cameraFragmentTextureView.surfaceTextureListener = textureListener
            }


            cameraPresenter.startBackgroundThread()
            isFragmentVisible = true
        } else {
            cameraPresenter.stopBackgroundThread()
            closeCamera()
            isFragmentVisible = false
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

                            captureRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                            updatePreview()
                        }

                    },
                    null
            )

        } catch (e: CameraAccessException) {
            Log.e(TAG, "error while create Camera Preview")
        }
    }

    private fun updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(
                    context, getString(R.string.error), Toast.LENGTH_SHORT
            ).show()
        }

        val requestBuilder = captureRequestBuilder

        if (requestBuilder != null) {
            try {
                cameraCaptureSessions
                        ?.setRepeatingRequest(
                                requestBuilder.build(), captureCallback, backgroundHandler

                        )
            } catch (e: CameraAccessException) {
                Log.e(TAG, "error while updatePreview")
            }
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
            imageDimension = map?.getOutputSizes(SurfaceTexture::class.java)?.get(0)

            transformImage(cameraFragmentTextureView.width.toFloat(), cameraFragmentTextureView.height.toFloat())

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
                                            .WRITE_EXTERNAL_STORAGE
                            ),
                                    REQUEST_CAMERA_PERMISSION_EXTRA_KEY
                            )
                }
                return
            }

            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            Log.e(TAG, "error while open camera")
        }

    }

    private fun transformImage(width: Float, height: Float) {
        if (cameraFragmentTextureView == null || imageDimension == null) {
            return
        }
        val dimension = imageDimension
        val matrix = Matrix()
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        val textureRectF = RectF(ZERO_FLOAT_KEY, ZERO_FLOAT_KEY, width, height)
        val previewRectF = dimension
                ?.width
                ?.toFloat()
                ?.let {
                    dimension
                            .height
                            .toFloat().let { it1 ->
                                RectF(
                                        ZERO_FLOAT_KEY,
                                        ZERO_FLOAT_KEY,
                                        it,
                                        it1
                                )
                            }
                }
        val centerX: Float = textureRectF.centerX()
        val centerY: Float = textureRectF.centerY()

        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            previewRectF?.offset(
                    centerX - previewRectF.centerX(),
                    centerY - previewRectF.centerY()
            )
            matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.FILL)

            val scale: Float = Math.max(
                    width / (dimension?.width ?: 0),
                    height / (dimension?.height ?: 0)
            )

            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate(
                    (90 * (rotation - 2)).toFloat(),
                    centerX, centerY
            )
        }
        cameraFragmentTextureView.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION_EXTRA_KEY
                && grantResults[0] != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                    context, getString(R.string.permissions_error), Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (isFragmentVisible) {
            cameraPresenter.startBackgroundThread()

            if (cameraFragmentTextureView.isAvailable) {
                openCamera()
            } else {
                cameraFragmentTextureView.surfaceTextureListener = textureListener
            }
        }
    }

    override fun onPause() {
        super.onPause()

        cameraPresenter.stopBackgroundThread()
    }

    private fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    override fun classifyFrame() {
        if (activity == null || cameraDevice == null) {
            setTypeText(getString(R.string.uninitialized_classifier))
            return
        }

        val bitmap: Bitmap =
                cameraFragmentTextureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X,
                        ImageClassifier.DIM_IMG_SIZE_Y)
        val textToShow = imageClassifier.classifyFrame(bitmap)

        bitmap.recycle()
        setTypeText(textToShow)
    }

    override fun setTypeText(type: String?) {
        activity?.runOnUiThread {
            if (type != null || activity != null) {
                cameraFragmentRestaurantType?.text = type
            } else {
                cameraFragmentRestaurantType?.text = getString(R.string.uninitialized_classifier)
            }
        }
    }

    interface IChangeFragmentCameraItemCallback {
        fun onItemChangedToMain()
    }

    interface IShowBottomSheetCallback {
        fun onShowBottomSheetFromCamera(restaurantType: String)
    }
}