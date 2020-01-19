package com.yzy.example.component.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
import android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION
import android.media.CamcorderProfile
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.example.android.camera2basic.CompareSizesByArea
import com.example.android.camera2basic.ImageSaver
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.extention.options
import com.yzy.example.widget.ControlView
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_camera_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.sqrt

class CameraFragment : CommFragment() {
    //拍照方向
    private val ORIENTATION = SparseIntArray()
    /*** 打开摄像头的ID[CameraDevice]. */
    private var mCameraId = CameraCharacteristics.LENS_FACING_FRONT
    private var mImageReader: ImageReader? = null

    private var picSavePath: String? = null //图片保存路径
    private var videoSavePath: String? = null //视频保存路径
    private var isCameraFront = false //当前是否是前置摄像头

    private var isLightOn = false //当前闪光灯是否开启

    companion object {
        fun startCameraFragment(controller: NavController, @IdRes id: Int) {
            controller.navigate(id, Bundle().apply { }, options)
        }
    }

    /*** 相机管理类 */
    override val contentLayout: Int = R.layout.fragment_camera_layout
    /*** 指定摄像头ID对应的Camera实体对象 */
    var mCameraDevice: CameraDevice? = null
    private var mCameraHandler: Handler? = null
    private var mMediaRecorder: MediaRecorder? = null

    private var imageReader: ImageReader? = null
    private lateinit var previewSize: Size
    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private val cameraOpenCloseLock = Semaphore(1)


    override fun initView(root: View?) {
        ORIENTATION.append(Surface.ROTATION_0, 90)
        ORIENTATION.append(Surface.ROTATION_90, 0)
        ORIENTATION.append(Surface.ROTATION_180, 270)
        ORIENTATION.append(Surface.ROTATION_270, 180)
        //MediaRecorder用于录像所需
        initTextTureView()
    }

    private var isOnShow: Boolean = false
    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if (isOnShow) {
            initTextTureView()
        }
        isOnShow = true
    }

    private fun initTextTureView() {
        if (textureView.isAvailable) {
            openCamera(textureView.width, textureView.height)
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    override fun initData() {
        //每次开启预览默认闪光灯没开启
        //每次开启预览默认闪光灯没开启
        isLightOn = false
        ivLightOn.isSelected = false
        ivLightOn.click {
            //开启关闭闪光灯
            openLight()
        }

        ivSwitchCamera.click {
            switchCamera()
        }
        ivTakePhoto.onRecordClickListener = object : ControlView.OnRecordClickListener {

            override fun onNoMinRecord(currentTime: Int) {
            }

            override fun onRecordFinished() {
                launch(Dispatchers.Main) {
                    stopRecordingVideo()
                }

            }

            override fun onRecordStart() {
                launch(Dispatchers.Main) {
                    startRecordingVideo()
                }
            }

            override fun onClick() {

            }
        }
    }

    /**
     * **********************************************切换摄像头**************************************
     */
    private fun switchCamera() {
        closeCamera()
        if (isCameraFront) {
            isCameraFront = false
            //后置摄像头
            mCameraId = CameraCharacteristics.LENS_FACING_FRONT
            openCamera(textureView.width, textureView.height)
        } else {
            //前置摄像头
            mCameraId = CameraCharacteristics.LENS_FACING_BACK
            isCameraFront = true
            openCamera(textureView.width, textureView.height)
        }
        ivLightOn.isVisible = !isCameraFront
    }

    /**
     * Closes the current [CameraDevice].
     * 关闭正在使用的相机
     */
    private fun closeCamera() { // 关闭捕获会话

        cameraOpenCloseLock.acquire()
        try {
            mCaptureSession?.let {
                it.close()
                mCaptureSession = null
            }
            mCameraDevice?.let {
                it.close()
                mCameraDevice = null
            }
            mImageReader?.let {
                it.close()
                mImageReader = null
            }
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }


    }

    /**
     * ***************************************打开和关闭闪光灯****************************************
     */
    private fun openLight() {
        if (isLightOn) {
            ivLightOn.isSelected = false
            isLightOn = false
            mPreviewRequestBuilder?.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF)
        } else {
            ivLightOn.isSelected = true
            isLightOn = true
            mPreviewRequestBuilder?.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
        }
        try {
            mPreviewRequestBuilder?.let {
                mCaptureSession?.setRepeatingRequest(
                    it.build(),
                    null,
                    mCameraHandler
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * TextureView 生命周期响应
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) = Unit
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        activity ?: return
        val rotation = mContext.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = max(
                viewHeight.toFloat() / previewSize.height,
                viewWidth.toFloat() / previewSize.width
            )
            with(matrix) {
                postScale(scale, scale, centerX, centerY)
                postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            }
        }
        textureView.setTransform(matrix)
    }

    /**
     * 打开指定摄像头ID的相机
     *
     * @param width
     * @param height
     * @param cameraId
     */
    private fun openCamera(width: Int, height: Int) {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val manager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            val characteristics = manager.getCameraCharacteristics(mCameraId.toString())
            val map = characteristics.get(SCALER_STREAM_CONFIGURATION_MAP)
                ?: throw RuntimeException("Cannot get available preview/video sizes")
            val rotation: Int = mContext.windowManager.defaultDisplay.rotation
            val totalRotation: Int = this.sensorToDeviceRotation(characteristics, rotation)
            val swapRotation = totalRotation == 90 || totalRotation == 270
            var rotatedWidth: Int = width
            var rotatedHeight: Int = height
            if (swapRotation) {
                rotatedWidth = height
                rotatedHeight = width
            }
            previewSize = getPreferredPreviewSize(
                map.getOutputSizes(SurfaceTexture::class.java),
                rotatedWidth,
                rotatedHeight
            )
            setupImageReader()
            configureTransform(width, height)
            manager.openCamera(mCameraId.toString(), mStateCallback, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    //handler
    private var mCameraThread: HandlerThread? = null

    /**
     * 初试化拍照线程
     */
    private fun startBackgroundThread() {
        mCameraThread = HandlerThread("CameraThread")
        mCameraThread?.let {
            it.start()
            mCameraHandler = Handler(it.looper)
        }

    }

    /**
     * 获取设备方向
     *
     * @param characteristics
     * @param deviceOrientation
     * @return
     */
    private fun sensorToDeviceRotation(
        characteristics: CameraCharacteristics,
        deviceOrientation: Int
    ): Int {
        val sensorOrientation = characteristics.get(SENSOR_ORIENTATION)!!
        val orientation: Int = ORIENTATION.get(deviceOrientation)
        return (sensorOrientation + orientation + 360) % 360
    }

    /**
     * 设置最佳尺寸
     *
     * @param sizes
     * @param width
     * @param height
     * @return
     */
    private fun getPreferredPreviewSize(
        sizes: Array<Size>,
        width: Int,
        height: Int
    ): Size {
        val collectorSizes: MutableList<Size> =
            ArrayList()
        for (option in sizes) {
            if (width > height) {
                if (option.width > width && option.height > height) {
                    collectorSizes.add(option)
                }
            } else {
                if (option.height > width && option.width > height) {
                    collectorSizes.add(option)
                }
            }
        }
        return if (collectorSizes.size > 0) {
            Collections.min(
                collectorSizes
            ) { s1, s2 -> java.lang.Long.signum(s1.width * s1.height - s2.width * s2.height.toLong()) }
        } else sizes[0]
    }

    /*** [CameraDevice.StateCallback]打开指定摄像头回调[CameraDevice] */
    private val mStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            mCameraDevice = cameraDevice
            startPreview()
            configureTransform(textureView.width, textureView.height)
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
        }
    }
    /*** [CaptureRequest.Builder]用于相机预览请求的构造器 */
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    /*** 用于相机预览的{@Link CameraCaptureSession}。 */
    private var mCaptureSession: CameraCaptureSession? = null

    /**
     * Start the camera preview.
     */
    private fun startPreview() {
        if (mCameraDevice == null || !textureView.isAvailable) return

        try {
            closePreviewSession()
            val texture = textureView.surfaceTexture
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)
            mPreviewRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            val previewSurface = Surface(texture)
            mPreviewRequestBuilder?.addTarget(previewSurface)

            mCameraDevice?.createCaptureSession(
                listOf(previewSurface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        mCaptureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                    }
                }, mCameraHandler
            )
        } catch (e: CameraAccessException) {
            Log.e(javaClass.name, e.toString())
        }

    }

    /**
     * Update the camera preview. [startPreview] needs to be called in advance.
     */
    private fun updatePreview() {
        if (mCameraDevice == null) return

        try {
            setUpCaptureRequestBuilder(mPreviewRequestBuilder)
            HandlerThread("CameraPreview").start()
            mPreviewRequestBuilder?.let {
                mCaptureSession?.setRepeatingRequest(it.build(), null, mCameraHandler)
            }
        } catch (e: CameraAccessException) {
            Log.e(javaClass.name, e.toString())
        }
    }

    private fun setUpCaptureRequestBuilder(builder: CaptureRequest.Builder?) {
        builder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }

    private fun closePreviewSession() {
        mCaptureSession?.close()
        mCaptureSession = null
    }


    /**
     * 使用Camera2录制和所拍的照片都会在这里
     */
    private fun getCamera2Path(filePath:String): String {
        val picturePath = Environment.getExternalStorageDirectory().absolutePath + "/CameraV2/"
        val file = File(picturePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return picturePath + filePath
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        mCameraThread?.quitSafely()
        try {
            mCameraThread?.join()
            mCameraThread = null
            mCameraHandler = null
        } catch (e: InterruptedException) {
        }
    }

    private fun startRecordingVideo() {
        if (mCameraDevice == null || !textureView.isAvailable) return

        try {
            closePreviewSession()
            setUpMediaRecorder()
            val texture = textureView.surfaceTexture.apply {
                setDefaultBufferSize(previewSize.width, previewSize.height)
            }

            val previewSurface = Surface(texture)
            val recorderSurface = mMediaRecorder!!.surface
            val surfaces = ArrayList<Surface>().apply {
                add(previewSurface)
                add(recorderSurface)
            }
            mPreviewRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
                    addTarget(previewSurface)
                    addTarget(recorderSurface)
                }
            mCameraDevice?.createCaptureSession(
                surfaces,
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        mCaptureSession = cameraCaptureSession
                        updatePreview()
                        activity?.runOnUiThread {
                            mMediaRecorder?.start()
                        }
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {

                    }
                }, mCameraHandler
            )
        } catch (e: CameraAccessException) {
            Log.e(javaClass.name, e.toString())
        } catch (e: IOException) {
            Log.e(javaClass.name, e.toString())
        }
    }


    @Throws(IOException::class)
    private fun setUpMediaRecorder() {
        val filename = "${System.currentTimeMillis()}.mp4"
        videoSavePath = getCamera2Path(filename)
        mMediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(videoSavePath)
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(previewSize.width, previewSize.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
        }
    }

    private fun stopRecordingVideo() {
        mMediaRecorder?.apply {
            stop()
            reset()
        }
        startPreview()
    }

    //配置ImageReader
    @SuppressLint("SimpleDateFormat")
    private fun setupImageReader() { //2代表ImageReader中最多可以获取两帧图像流
        mImageReader =
            ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 2)
        mImageReader?.setOnImageAvailableListener({ reader ->
            val mImage = reader.acquireNextImage()
            val buffer = mImage.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer[data]
            val filename = "IMG_" +  SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg"
            videoSavePath = getCamera2Path(filename)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(picSavePath)
                fos.write(data, 0, data.size)
                val msg = Message()
//                msg.what = CAPTURE_OK
                msg.obj = picSavePath
                mCameraHandler?.sendMessage(msg)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            mImage.close()
        }, mCameraHandler)
    }

}
