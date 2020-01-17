package com.yzy.example.component.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.extention.options
import com.yzy.example.widget.VideoControlView
import kotlinx.android.synthetic.main.fragment_camera_layout.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : CommFragment() {
    //拍照方向
    private val ORIENTATION = SparseIntArray()
    /*** 打开摄像头的ID[CameraDevice]. */
    private var mCameraId = CameraCharacteristics.LENS_FACING_FRONT
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0
    /*** 用于运行不应阻塞UI的任务的附加线程。 */
    private var mBackgroundThread: HandlerThread? = null
    private var mPreviewSize: Size? = null
    /***判断是否支持闪关灯 */
    private var mFlashSupported = false
    private val CAPTURE_OK = 0 //拍照完成回调
    private var mImageReader: ImageReader? = null

    var PATH_SAVE_VIDEO: String = getCamera2Path() //小视频存放地址，不设置的话默认在根目录的Camera2文件夹

    var PATH_SAVE_PIC: String = getCamera2Path() //图片保存地址，不设置的话默认在根目录的Camera2文件夹
    private var picSavePath: String? = null //图片保存路径
    private val videoSavePath: String? = null //视频保存路径

    var ACTIVITY_AFTER_CAPTURE: Class<*>? = null
    //拍照完成后需要跳转的Activity,一般这个activity做处理照片或者视频用
    var INTENT_PATH_SAVE_VIDEO = "INTENT_PATH_SAVE_VIDEO" //Intent跳转可用

    var INTENT_PATH_SAVE_PIC = "INTENT_PATH_SAVE_PIC" //Intent跳转可用
    private var isCameraFront = false //当前是否是前置摄像头

    private var isLightOn = false //当前闪光灯是否开启

    companion object {
        fun startCameraFragment(controller: NavController, @IdRes id: Int) {
            controller.navigate(id, Bundle().apply { }, options)
        }
    }

    /*** 相机管理类 */
    var mCameraManager: CameraManager? = null
    override val contentLayout: Int = R.layout.fragment_camera_layout
    /*** 指定摄像头ID对应的Camera实体对象 */
    var mCameraDevice: CameraDevice? = null
    private var mCameraHandler: Handler? = null
    override fun initView(root: View?) {
        ORIENTATION.append(Surface.ROTATION_0, 90)
        ORIENTATION.append(Surface.ROTATION_90, 0)
        ORIENTATION.append(Surface.ROTATION_180, 270)
        ORIENTATION.append(Surface.ROTATION_270, 180)
        mCameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        if (textureView.isAvailable) {
            openCamera(textureView.width, textureView.height, mCameraId)
        } else {
            textureView.surfaceTextureListener = textureListener
        }
        startBackgroundThread()
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
        ivTakePhoto.setOnRecordListener(object : VideoControlView.OnRecordListener() {
            override fun onShortClick() {

            }

            override fun OnRecordStartClick() {
            }

            override fun OnFinish(resultCode: Int) {

            }
        })
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
            openCamera(mSurfaceWidth, mSurfaceHeight, mCameraId)
        } else {
            //前置摄像头
            mCameraId = CameraCharacteristics.LENS_FACING_BACK
            isCameraFront = true
            openCamera(mSurfaceWidth, mSurfaceHeight, mCameraId)
        }
        ivLightOn.isVisible = !isCameraFront
    }

    /**
     * Closes the current [CameraDevice].
     * 关闭正在使用的相机
     */
    private fun closeCamera() { // 关闭捕获会话
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
    private val textureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        //创建
        override fun onSurfaceTextureAvailable(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) { //当TextureView创建完成，打开指定摄像头相机
            openCamera(width, height, mCameraId)
        }

        //尺寸改变
        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {
        }

        //销毁
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        //更新
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    /**
     * 打开指定摄像头ID的相机
     *
     * @param width
     * @param height
     * @param cameraId
     */
    private fun openCamera(width: Int, height: Int, cameraId: Int) {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) { // TODO: Consider calling
            return
        }
        try {
            mSurfaceWidth = width
            mSurfaceHeight = height
            mCameraManager?.let {
                val characteristics: CameraCharacteristics =
                    it.getCameraCharacteristics(mCameraId.toString() + "")
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                // 获取设备方向
                val rotation: Int = mContext.windowManager.defaultDisplay.rotation
                val totalRotation: Int =
                    this.sensorToDeviceRotation(characteristics, rotation)
                val swapRotation = totalRotation == 90 || totalRotation == 270
                var rotatedWidth: Int = mSurfaceWidth
                var rotatedHeight: Int = mSurfaceHeight
                if (swapRotation) {
                    rotatedWidth = mSurfaceHeight
                    rotatedHeight = mSurfaceWidth
                }
                // 获取最佳的预览尺寸
                map?.let { configMap ->
                    mPreviewSize = getPreferredPreviewSize(
                        configMap.getOutputSizes(
                            SurfaceTexture::class.java
                        ), rotatedWidth, rotatedHeight
                    )
                }

                setupImageReader()
                //检查是否支持闪光灯
                val available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                mFlashSupported = available ?: false
                it.openCamera(mCameraId.toString() + "", mStateCallback, null)
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    //配置ImageReader
    private fun setupImageReader() { //2代表ImageReader中最多可以获取两帧图像流
        mPreviewSize?.let {
            mImageReader = ImageReader.newInstance(it.width, it.height, ImageFormat.JPEG, 2)
        }
        mImageReader?.setOnImageAvailableListener({ reader ->
            val mImage = reader.acquireNextImage()
            val buffer = mImage.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer[data]
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            createSavePath(PATH_SAVE_PIC) //判断有没有这个文件夹，没有的话需要创建
            picSavePath = PATH_SAVE_PIC + "IMG_" + timeStamp + ".jpg"
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(picSavePath)
                fos.write(data, 0, data.size)
                val msg = Message()
                msg.what = CAPTURE_OK
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
        mCameraHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    CAPTURE_OK ->  //这里拍照保存完成，可以进行相关的操作，例如再次压缩等(由于封装，这里我先跳转掉完成页面)
                        if (ACTIVITY_AFTER_CAPTURE != null) {
//                            val intent = Intent(
//                                this@Camera2RecordActivity,
//                                Camera2Config.ACTIVITY_AFTER_CAPTURE
//                            )
//                            intent.putExtra(Camera2Config.INTENT_PATH_SAVE_PIC, picSavePath)
//                            startActivity(intent)
                        }
                }
            }
        }
    }

    //handler
    private var mCameraThread: HandlerThread? = null

    /**
     * 初试化拍照线程
     */
    fun startBackgroundThread() {
//        mCameraThread = HandlerThread("CameraThread")
//        mCameraThread.start()
//        mCameraHandler = Handler(mCameraThread.getLooper())
//        mTextureView.setSurfaceTextureListener(this)
//
//        mBackgroundThread = HandlerThread("Camera Background")
//        mBackgroundThread?.let {
//            it.start()
//            mCameraHandler = Handler(it.looper)
//        }
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
        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
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
    ): Size? {
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
            mCameraDevice = cameraDevice
            createCameraPreview()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraDevice.close()
        }
    }
    /*** [CaptureRequest.Builder]用于相机预览请求的构造器 */
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    /*** 用于相机预览的{@Link CameraCaptureSession}。 */
    private var mCaptureSession: CameraCaptureSession? = null

    /**
     * 创建预览对话
     */
    private fun createCameraPreview() {
        try { // 获取texture实例
            val surfaceTexture: SurfaceTexture = textureView.surfaceTexture
            //我们将默认缓冲区的大小配置为我们想要的相机预览的大小。
            surfaceTexture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
            // 用来开始预览的输出surface
            val surface = Surface(surfaceTexture)
            //创建预览请求构建器
            mPreviewRequestBuilder =
                mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //将TextureView的Surface作为相机的预览显示输出
            mPreviewRequestBuilder?.addTarget(surface)
            //在这里，我们为相机预览创建一个CameraCaptureSession。
            mCameraDevice?.createCaptureSession(
                listOf(surface, mImageReader!!.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) { // 相机关闭时, 直接返回
                        if (null == mCameraDevice) {
                            return
                        }
                        //会话准备就绪后，我们开始显示预览。
// 会话可行时, 将构建的会话赋给field
                        mCaptureSession = cameraCaptureSession
                        //相机预览应该连续自动对焦。
                        mPreviewRequestBuilder?.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                        )

                        // 构建上述的请求
                        mPreviewRequest = mPreviewRequestBuilder?.build()
                        // 重复进行上面构建的请求, 用于显示预览
                        try {
                            mPreviewRequest?.let {
                                mCaptureSession?.setRepeatingRequest(it, null, mCameraHandler)
                            }

                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
//                        showToast("预览失败了")
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /***预览请求, 由上面的构建器构建出来 */
    private var mPreviewRequest: CaptureRequest? = null

    /**
     * 使用Camera2录制和所拍的照片都会在这里
     */
    private fun getCamera2Path(): String {
        val picturePath = Environment.getExternalStorageDirectory().absolutePath + "/CameraV2/"
        val file = File(picturePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return picturePath
    }

    /**
     * 判断传入的地址是否已经有这个文件夹，没有的话需要创建
     */
    fun createSavePath(path: String?) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}
