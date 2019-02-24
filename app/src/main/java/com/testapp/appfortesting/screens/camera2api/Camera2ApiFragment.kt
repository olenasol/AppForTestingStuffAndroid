package com.testapp.appfortesting.screens.camera2api


import android.Manifest
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera2_api.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import com.testapp.appfortesting.R
import java.io.FileOutputStream


class Camera2ApiFragment : Fragment() {

    var cameraManager: CameraManager? = null
    var surfaceTextureListener: TextureView.SurfaceTextureListener? = null
    var cameraFacing = CameraCharacteristics.LENS_FACING_BACK
    var cameraId: String = "0"
    var backgroundHandler: Handler? = null
    lateinit var stateCallback: CameraDevice.StateCallback
    var cameraDevice: CameraDevice? = null
    lateinit var previewSize: Size
    var backgroundThread: HandlerThread? = null
    var cameraCaptureSession: CameraCaptureSession? = null
    var captureRequestBuilder: CaptureRequest.Builder? = null
    lateinit var galleryFolder:File

    companion object {

        const val CAMERA_REQUEST_CODE: Int = 0

        fun newInstance(): Camera2ApiFragment {
            return Camera2ApiFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                this@Camera2ApiFragment.cameraDevice = cameraDevice
                createPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                this@Camera2ApiFragment.cameraDevice = null
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                this@Camera2ApiFragment.cameraDevice = null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.testapp.appfortesting.R.layout.fragment_camera2_api, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), CAMERA_REQUEST_CODE
        )

        btnTakePhoto.setOnClickListener {
            createImageGallery()
            onTakePhotoButtonClicked()
        }
        cameraManager = context?.getSystemService(CAMERA_SERVICE) as CameraManager?

        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                setUpCamera()
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {

            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {

            }
        }
    }


    override fun onResume() {
        super.onResume()
        openBackgroundThread()
        if (textureView.isAvailable()) {
            setUpCamera()
            openCamera()
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener)
        }
    }

    override fun onStop() {
        super.onStop()
        closeCamera()
        closeBackgroundThread()
    }

    private fun createPreviewSession() {
        try {
            val surfaceTexture = textureView.getSurfaceTexture()
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight())
            val previewSurface = Surface(surfaceTexture)
            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(
                Collections.singletonList(previewSurface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (cameraDevice ==
                            null
                        ) {
                            return
                        }

                        try {
                            val captureRequest = captureRequestBuilder?.build()
                            this@Camera2ApiFragment.cameraCaptureSession = cameraCaptureSession
                            this@Camera2ApiFragment.cameraCaptureSession!!.setRepeatingRequest(
                                captureRequest!!,
                                null,
                                backgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {

                    }
                }, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession!!.close()
            cameraCaptureSession = null
        }

        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }

    private fun closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread?.quitSafely()
            backgroundThread = null
            backgroundHandler = null
        }
    }

    private fun openBackgroundThread() {
        backgroundThread = HandlerThread("camera_background_thread")
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread?.getLooper())
    }

    fun setUpCamera() {
        try {
            for (cameraId in cameraManager!!.getCameraIdList()) {
                val cameraCharacteristics = (cameraManager as CameraManager).getCameraCharacteristics(cameraId)
                if ((cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing)) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )
                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
                    this.cameraId = cameraId
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cameraManager?.openCamera(cameraId, stateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun createImageGallery() {
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        galleryFolder = File(storageDirectory, resources.getString(R.string.app_name))
        if (!galleryFolder.exists()) {
            val wasCreated = galleryFolder.mkdirs()
            if (!wasCreated) {
                Log.e("CapturedImages", "Failed to create directory")
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(galleryFolder: File): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

    private fun lock() {
        try {
            cameraCaptureSession?.capture(
                captureRequestBuilder!!.build(),
                null, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun unlock() {
        try {
            cameraCaptureSession?.setRepeatingRequest(
                captureRequestBuilder!!.build(),
                null, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    fun onTakePhotoButtonClicked() {
        lock()
        var outputPhoto: FileOutputStream? = null
        try {
            outputPhoto = FileOutputStream(createImageFile(galleryFolder))
            textureView.bitmap
                .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            unlock()
            try {
                if (outputPhoto != null) {
                    outputPhoto.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}
