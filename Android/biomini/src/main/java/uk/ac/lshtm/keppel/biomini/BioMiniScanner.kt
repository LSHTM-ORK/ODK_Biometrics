package uk.ac.lshtm.keppel.biomini

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import com.suprema.BioMiniFactory
import com.suprema.CaptureResponder
import com.suprema.IBioMiniDevice
import com.suprema.IUsbEventHandler
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.toHexString
import java.util.concurrent.CountDownLatch

private const val TAG = "KeppelBioMiniScanner"

@Suppress("unused")
class BioMiniScanner(private val context: Context) : Scanner {

    private val BASE_EVENT = 3000
    private val ACTIVATE_USB_DEVICE = BASE_EVENT + 1
    private val REMOVE_USB_DEVICE = BASE_EVENT + 2
    private val UPDATE_DEVICE_INFO = BASE_EVENT + 3
    private val REQUEST_USB_PERMISSION = BASE_EVENT + 4
    private val MAKE_DELAY_1SEC = BASE_EVENT + 5
    private val ADD_DEVICE = BASE_EVENT + 6
    private val CLEAR_VIEW_FOR_CAPTURE = BASE_EVENT + 8
    private val SET_TEXT_LOGVIEW = BASE_EVENT + 10
    private val MAKE_TOAST = BASE_EVENT + 11
    private val SHOW_CAPTURE_IMAGE_DEVICE = BASE_EVENT + 12

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    var mUsbDevice: UsbDevice? = null
    private var mUsbManager: UsbManager? = null
    private var mBioMiniFactory: BioMiniFactory? = null
    var mCurrentDevice: IBioMiniDevice? = null
    private val mCaptureOption: IBioMiniDevice.CaptureOption = IBioMiniDevice.CaptureOption()
    private lateinit var onConnected: () -> Unit
    private var mPermissionIntent: PendingIntent? = null
    private var mTemplateData: IBioMiniDevice.TemplateData? = null
    private var mFpQuality: Int? = null

    private var onDisconnected: (() -> Unit)? = null

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.getAction()
            when (action) {
                ACTION_USB_PERMISSION -> {
                    Log.d(TAG, "ACTION_USB_PERMISSION")
                    val hasUsbPermission: Boolean =
                        intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                    val usbDevice = mUsbDevice
                    if (hasUsbPermission && usbDevice != null) {
                        Log.d(TAG, usbDevice.getDeviceName() + " is acquire the usb permission. activate this device.")
                        mHandler.sendEmptyMessage(ACTIVATE_USB_DEVICE)
                    } else {
                        Log.d(TAG, "USB permission is not granted!")
                    }
                }
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    Log.d(TAG, "ACTION_USB_DEVICE_ATTACHED")
                    initUsbDevice()
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    Log.d(TAG, "ACTION_USB_DEVICE_DETACHED")
                    removeDevice()
                    onDisconnected?.invoke()
                }
                else -> {}
            }
        }
    }

    private fun initUsbListener() {
        Log.d(TAG, "start initUsbListener!")
        context.registerReceiver(
            mUsbReceiver,
            IntentFilter(ACTION_USB_PERMISSION),
        )
        val attachfilter = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        context.registerReceiver(mUsbReceiver, attachfilter)
        val detachfilter = IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED)
        context.registerReceiver(mUsbReceiver, detachfilter)
    }

    var mHandler: android.os.Handler = object : android.os.Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                ACTIVATE_USB_DEVICE -> {
                    Log.d(TAG, "got ACTIVATE_USB_DEVICE")
                    createBioMiniDevice()
                }
                REQUEST_USB_PERMISSION -> {
                    Log.d(TAG, "got REQUEST_USB_PERMISSION")
                    // https://developer.android.com/reference/android/app/PendingIntent#FLAG_MUTABLE
                    var FLAG_MUTABLE = 0 // PendingIntent.FLAG_MUTABLE
                    if (Build.VERSION.SDK_INT >= 31) { // Build.VERSION_CODES.S
                        FLAG_MUTABLE = PendingIntent.FLAG_MUTABLE
                    }

                    mPermissionIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(ACTION_USB_PERMISSION),
                        FLAG_MUTABLE,
                    )
                    mUsbManager?.requestPermission(mUsbDevice, mPermissionIntent)
                }
            }
        }
    }

    fun initUsbDevice() {
        Log.d("TAG", "start!")
        val usbManager = mUsbManager
        if (usbManager == null) {
            Log.d(TAG, "mUsbManager is null")
            return
        }
        if (mUsbDevice != null) {
            Log.d(TAG, "usbdevice is not null!")
            return
        }
        val deviceList: java.util.HashMap<String, UsbDevice> = usbManager.getDeviceList()
        val deviceIter: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIter.hasNext()) {
            val _device: UsbDevice = deviceIter.next()
            Log.d(TAG, "device id" + _device.getVendorId())
            if (_device.getVendorId() == 0x16d1) {
                Log.d(TAG, "found suprema usb device")
                mUsbDevice = _device
                if (usbManager.hasPermission(_device) == false) {
                    Log.d(TAG, "This device need to Usb Permission!")
                    mHandler.sendEmptyMessage(REQUEST_USB_PERMISSION)
                } else {
                    Log.d(
                        TAG,
                        "This device alread have USB permission! please activate this device.",
                    )
                    mHandler.sendEmptyMessage(ACTIVATE_USB_DEVICE)
                }
            } else {
                Log.d(TAG, "This device is not suprema device!  : " + _device.getVendorId())
            }
        }
    }

    private fun removeDevice() {
        Log.d(TAG, "ACTION_USB_DEVICE_DETACHED")
        val factory = mBioMiniFactory
        if (factory != null) {
            factory.removeDevice(mUsbDevice)
            factory.close()
        }
        mUsbDevice = null
        mCurrentDevice = null
    }

    private fun createBioMiniDevice() {
        mBioMiniFactory?.close()
        mBioMiniFactory = object : BioMiniFactory(context, mUsbManager) {
            override fun onDeviceChange(event: IUsbEventHandler.DeviceChangeEvent, dev: Any?) {
                Log.d(TAG, "onDeviceChange : $event")
            }
        }
        Log.d(TAG, "new BioMiniFactory( ) : $mBioMiniFactory")
        mBioMiniFactory?.setTransferMode(IBioMiniDevice.TransferMode.MODE2)
        val _result: Boolean? = mBioMiniFactory?.addDevice(mUsbDevice)
        if (_result == true) {
            mCurrentDevice = mBioMiniFactory?.getDevice(0)
            if (mCurrentDevice != null) {
                onConnected()
                Log.d(TAG, "mCurrentDevice attached : $mCurrentDevice")
            } else {
                Log.d(TAG, "mCurrentDevice is null")
            }
        } else {
            Log.d(TAG, "addDevice is fail!")
        }
    }

    override fun connect(onConnected: () -> Unit): Scanner {
        getDeviceName().let { Log.d(TAG, "Device name: $it") }
        this.onConnected = onConnected
        // ** NOTE **
        // The call to setIoStatus() may need to be commented out when using an external USB reader.
        if (getDeviceName() == "EACRUGGED RG80") {
            Log.d(TAG, "Calling setIoStatus() in connect")
            IoControl.getInstance().setIoStatus(
                IoControl.USBFP_PATH,
                IoControl.IOSTATUS.ENABLE,
            )
        }
        if (mUsbManager == null) {
            mUsbManager =
                context.getSystemService(Context.USB_SERVICE) as UsbManager
        }
        initUsbListener()
        initUsbDevice()
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {
        this.onDisconnected = onDisconnected
    }

    private fun getCaptureCallback(latch: CountDownLatch): CaptureResponder {
        return object : CaptureResponder() {
            override fun onCapture(context: Any?, fingerState: IBioMiniDevice.FingerState?) {
                super.onCapture(context, fingerState)
            }

            override fun onCaptureEx(
                context: Any?,
                option: IBioMiniDevice.CaptureOption,
                capturedImage: Bitmap?,
                capturedTemplate: IBioMiniDevice.TemplateData?,
                fingerState: IBioMiniDevice.FingerState?,
            ): Boolean {
                Log.d(TAG, "START! : " + mCaptureOption.captureFuntion.toString())
                val currentDevice = mCurrentDevice
                if (capturedTemplate != null) {
                    Log.d(TAG, "TemplateData is not null!")
                    mTemplateData = capturedTemplate
                }
                if (currentDevice != null) {
                    val imageData: ByteArray? = currentDevice.getCaptureImageAsRAW_8()
                    if (imageData != null) {
                        val mode: IBioMiniDevice.FpQualityMode =
                            IBioMiniDevice.FpQualityMode.NQS_MODE_NFIQ
                        mFpQuality = currentDevice.getFPQuality(
                            imageData,
                            currentDevice.getImageWidth(),
                            currentDevice.getImageHeight(),
                            mode.value(),
                        )
                        Log.d(TAG, "mFpQuality : $mFpQuality")
                    }
                }
                latch.countDown()
                return true
            }

            override fun onCaptureError(context: Any?, errorCode: Int, error: String) {
                // TODO error handling
                //            if (errorCode == IBioMiniDevice.ErrorCode.CTRL_ERR_IS_CAPTURING.value()) {
                //                setLogInTextView("Other capture function is running. abort capture function first!")
                //            } else if (errorCode == IBioMiniDevice.ErrorCode.CTRL_ERR_CAPTURE_ABORTED.value()) {
                //                Log.d(TAG, "CTRL_ERR_CAPTURE_ABORTED occured.")
                //            } else if (errorCode == IBioMiniDevice.ErrorCode.CTRL_ERR_FAKE_FINGER.value()) {
                //                setLogInTextView("Fake Finger Detected")
                //                if (mCurrentDevice != null && mCurrentDevice.getLfdLevel() > 0) {
                //                    setLogInTextView("LFD SCORE : " + mCurrentDevice.getLfdScoreFromCapture())
                //                }
                //            } else {
                //                setLogInTextView(mCaptureOption.captureFuntion.name() + " is fail by " + error)
                //                setLogInTextView("Please try again.")
                //            }
                //            mViewPager.setUserInputEnabled(true)
                //            setUiClickable(true)
            }
        }
    }

    private fun doSingleCapture(latch: CountDownLatch) {
        Log.d(TAG, "START!")
        mTemplateData = null
        mCaptureOption.captureFuntion = IBioMiniDevice.CaptureFuntion.CAPTURE_SINGLE
        mCaptureOption.extractParam.captureTemplate = true
        mCaptureOption.extractParam.maxTemplateSize =
            IBioMiniDevice.MaxTemplateSize.MAX_TEMPLATE_1024
        val device = mCurrentDevice
        if (device != null) {
            val result: Boolean = device.captureSingle(
                mCaptureOption,
                getCaptureCallback(latch),
                true,
            )
            if (result != true) Log.d(TAG, "capture failed")
        }
    }

    private fun setTemplateType() {
        val currentDevice = mCurrentDevice
        if (currentDevice != null) {
            val tmp_type = IBioMiniDevice.TemplateType.ISO19794_2.value()
            currentDevice.setParameter(
                IBioMiniDevice.Parameter(
                    IBioMiniDevice.ParameterType.TEMPLATE_TYPE,
                    tmp_type.toLong(),
                ),
            )
        }
    }

    override fun capture(): CaptureResult {
        val latch = CountDownLatch(1)
        setTemplateType()
        doSingleCapture(latch)
        latch.await()
        val tmp = mTemplateData
        if (tmp?.data != null) {
            Log.d(TAG, tmp.data.size.toString())
            Log.d(TAG, tmp.data.toHexString())
            return CaptureResult(tmp.data.toHexString(), mFpQuality ?: 0)
        }
        // TODO error handling
        return CaptureResult("no-template", 0)
    }

    override fun stopCapture() {
        val device = mCurrentDevice
        if (device != null) {
            if (!device.isCapturing()) {
                mCaptureOption.captureFuntion = IBioMiniDevice.CaptureFuntion.NONE
                return
            }
            val result: Int = device.abortCapturing()
            Log.d(TAG, "run: abortCapturing : $result")
            if (result == 0) {
                mCaptureOption.captureFuntion = IBioMiniDevice.CaptureFuntion.NONE
            }
        }
    }

    override fun disconnect() {
        var result = 0
        val factory = mBioMiniFactory // avoid null errors
        if (factory != null) {
            if (mUsbDevice != null) result = factory.removeDevice(mUsbDevice)
            if (result == IBioMiniDevice.ErrorCode.OK.value() || result == IBioMiniDevice.ErrorCode.ERR_NO_DEVICE.value()) {
                factory.close()
                context.unregisterReceiver(mUsbReceiver)
                mUsbDevice = null
                mCurrentDevice = null
            }
        }

        removeDevice()
    }
}

fun getDeviceName(): String {
    val manufacturer: String = Build.MANUFACTURER
    val model: String = Build.MODEL
    return if (model.lowercase(java.util.Locale.getDefault())
            .startsWith(manufacturer.lowercase(java.util.Locale.getDefault()))
    ) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}

private fun capitalize(s: String?): String {
    if (s == null || s.length == 0) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        first.uppercaseChar().toString() + s.substring(1)
    }
}
