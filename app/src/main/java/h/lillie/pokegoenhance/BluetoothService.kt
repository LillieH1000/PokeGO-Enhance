package h.lillie.pokegoenhance

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@SuppressLint("InflateParams")
class BluetoothService : AccessibilityService() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayHandler: Handler
    private lateinit var scope: Job

    private lateinit var giftsView: View
    private lateinit var sendView: View
    private lateinit var openView: View

    private var inPokemonGO: Boolean = false

    override fun onServiceConnected() {
        super.onServiceConnected()

        // Set Window Manager

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        giftsView = LayoutInflater.from(this@BluetoothService).inflate(R.layout.gifts, null, false)
        sendView = LayoutInflater.from(this@BluetoothService).inflate(R.layout.send, null, false)
        openView = LayoutInflater.from(this@BluetoothService).inflate(R.layout.open, null, false)

        val sendGift: MaterialButton = giftsView.findViewById(R.id.sendGift)
        sendGift.setOnClickListener {
            // Auto Touch
            if (this@BluetoothService::scope.isInitialized && scope.isActive) {
                scope.cancel()
                return@setOnClickListener
            }
            // Open View
            if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                windowManager.removeViewImmediate(openView)
            }
            // Send View
            if (this@BluetoothService::sendView.isInitialized && sendView.parent == null) {
                addView(sendView, 1, 190)
                return@setOnClickListener
            }
            if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                windowManager.removeViewImmediate(sendView)
                return@setOnClickListener
            }
        }

        val openGift: MaterialButton = giftsView.findViewById(R.id.openGift)
        openGift.setOnClickListener {
            // Auto Touch
            if (this@BluetoothService::scope.isInitialized && scope.isActive) {
                scope.cancel()
                return@setOnClickListener
            }
            // Send View
            if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                windowManager.removeViewImmediate(sendView)
            }
            // Open View
            if (this@BluetoothService::openView.isInitialized && openView.parent == null) {
                addView(openView, 1, 120)
                return@setOnClickListener
            }
            if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                windowManager.removeViewImmediate(openView)
                return@setOnClickListener
            }
        }

        val send: MaterialButton = sendView.findViewById(R.id.start)
        send.setOnClickListener {
            val topSwitch: SwitchMaterial = sendView.findViewById(R.id.top)
            val input: EditText = sendView.findViewById(R.id.input)
            val limit: Int? = input.text.toString().toIntOrNull()
            if (limit != null && limit > 0) {
                // Send View
                if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                    windowManager.removeViewImmediate(sendView)
                }
                // Open View
                if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                    windowManager.removeViewImmediate(openView)
                }
                var count = 0
                scope = CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        ensureActive()
                        suspend fun dispatch(x: Float, y: Float) = withContext(Dispatchers.Default) {
                            val path = Path()

                            path.reset()
                            path.moveTo(x, y)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            if (topSwitch.isChecked) {
                                path.reset()
                                path.moveTo(545f, 2191f)
                                dispatchGesture(GestureDescription.Builder()
                                    .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                    .build(), null, null)
                                delay(3000)
                            }

                            path.reset()
                            path.moveTo(283f, 1802f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(626f, 847f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(544f, 1989f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(1000)

                            path.reset()
                            path.moveTo(545f, 2191f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(545f, 2191f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)
                        }

                        // Friend 1
                        dispatch(688f, 939f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 2
                        dispatch(526f, 1296f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 3
                        dispatch(602f, 1604f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 4
                        dispatch(569f, 1991f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        } else {
                            val scrollPath = Path()
                            scrollPath.reset()
                            scrollPath.moveTo(250f, 1000f)
                            scrollPath.lineTo(250f, 500f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(scrollPath, 0, 400))
                                .build(), null, null)
                            delay(1000)
                        }
                    }
                }
            }
        }

        val open: MaterialButton = openView.findViewById(R.id.start)
        open.setOnClickListener {
            val input: EditText = openView.findViewById(R.id.input)
            val limit: Int? = input.text.toString().toIntOrNull()
            if (limit != null && limit > 0) {
                // Send View
                if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                    windowManager.removeViewImmediate(sendView)
                }
                // Open View
                if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                    windowManager.removeViewImmediate(openView)
                }
                var count = 0
                scope = CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        ensureActive()
                        suspend fun dispatch(x: Float, y: Float) = withContext(Dispatchers.Default) {
                            val path = Path()

                            path.reset()
                            path.moveTo(x, y)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(540f, 1599f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(548f, 2010f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(1000)

                            path.reset()
                            path.moveTo(545f, 2191f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)

                            path.reset()
                            path.moveTo(545f, 2191f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                .build(), null, null)
                            delay(3000)
                        }

                        // Friend 1
                        dispatch(688f, 939f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 2
                        dispatch(526f, 1296f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 3
                        dispatch(602f, 1604f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        }
                        // Friend 4
                        dispatch(569f, 1991f)
                        count += 1
                        if (count == limit) {
                            scope.cancel()
                        } else {
                            val scrollPath = Path()
                            scrollPath.reset()
                            scrollPath.moveTo(250f, 1000f)
                            scrollPath.lineTo(250f, 500f)
                            dispatchGesture(GestureDescription.Builder()
                                .addStroke(GestureDescription.StrokeDescription(scrollPath, 0, 400))
                                .build(), null, null)
                            delay(1000)
                        }
                    }
                }
            }
        }

        overlayHandler = Handler(Looper.getMainLooper())
        overlayHandler.post(overlayTask)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.contentChangeTypes != AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED || event.packageName == null || event.className == null) return

        if (event.packageName == "com.android.settings" && event.className == "com.android.settings.bluetooth.BluetoothPairingDialog") return

        // PokeGO Plus Auto Pair
        if (event.packageName == "com.android.settings" && Regex("[^A-Za-z0-9 ]").replace(event.text.toString(), "").contains("pair with pokemon go plus", true) && event.source != null) {
            val pairButtonList = event.source!!.findAccessibilityNodeInfosByViewId("android:id/button1")
            if (pairButtonList.isNotEmpty()) {
                pairButtonList[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }

        if (event.packageName == "com.android.systemui" && Regex("[^A-Za-z0-9 ]").replace(event.text.toString(), "").contains("lock screen", true) && event.source != null) {
            inPokemonGO = false
            // Auto Touch
            if (this@BluetoothService::scope.isInitialized && scope.isActive) {
                scope.cancel()
            }
            // Gifts View
            if (this@BluetoothService::giftsView.isInitialized && giftsView.parent != null) {
                windowManager.removeViewImmediate(giftsView)
            }
            // Send View
            if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                windowManager.removeViewImmediate(sendView)
            }
            // Open View
            if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                windowManager.removeViewImmediate(openView)
            }
        }

        try {
            packageManager.getActivityInfo(ComponentName(event.packageName.toString(), event.className.toString()), 0)
            if (event.packageName == "com.nianticlabs.pokemongo") {
                inPokemonGO = true
            } else {
                inPokemonGO = false
                // Auto Touch
                if (this@BluetoothService::scope.isInitialized && scope.isActive) {
                    scope.cancel()
                }
                // Gifts View
                if (this@BluetoothService::giftsView.isInitialized && giftsView.parent != null) {
                    windowManager.removeViewImmediate(giftsView)
                }
                // Send View
                if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                    windowManager.removeViewImmediate(sendView)
                }
                // Open View
                if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                    windowManager.removeViewImmediate(openView)
                }
            }
        } catch (_: PackageManager.NameNotFoundException) {
        }
    }

    override fun onInterrupt() {
    }

    private fun addView(view: View, align: Int, height: Int) {
        val windowManagerLayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        windowManagerLayoutParams.format = PixelFormat.TRANSLUCENT

        if (align == 0) {
            windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            windowManagerLayoutParams.gravity = Gravity.TOP or Gravity.CENTER
        }
        if (align == 1) {
            windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            windowManagerLayoutParams.gravity = Gravity.TOP or Gravity.CENTER
            windowManagerLayoutParams.y = 70 * (this@BluetoothService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        windowManagerLayoutParams.height = height * (this@BluetoothService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        windowManagerLayoutParams.width = 400 * (this@BluetoothService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        windowManager.addView(view, windowManagerLayoutParams)
    }

    private val overlayTask = object : Runnable {
        override fun run() {
            if (inPokemonGO) {
                takeScreenshot(Display.DEFAULT_DISPLAY, Executors.newSingleThreadExecutor(), object : TakeScreenshotCallback {
                    override fun onSuccess(screenshot: ScreenshotResult) {
                        val bitmap: Bitmap = Bitmap.wrapHardwareBuffer(screenshot.hardwareBuffer, screenshot.colorSpace)!!
                        val image = InputImage.fromBitmap(bitmap, 0)
                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                if (Regex("[^A-Za-z0-9 ]").replace(visionText.text.replace("\n", ""), "").contains("FRIENDS") || sendView.parent != null || openView.parent != null || (this@BluetoothService::scope.isInitialized && scope.isActive)) {
                                    // Gifts View
                                    if (inPokemonGO && this@BluetoothService::giftsView.isInitialized && giftsView.parent == null) {
                                        addView(giftsView, 0, 50)
                                    }
                                } else {
                                    // Gifts View
                                    if (this@BluetoothService::giftsView.isInitialized && giftsView.parent != null) {
                                        windowManager.removeViewImmediate(giftsView)
                                    }
                                    // Send View
                                    if (this@BluetoothService::sendView.isInitialized && sendView.parent != null) {
                                        windowManager.removeViewImmediate(sendView)
                                    }
                                    // Open View
                                    if (this@BluetoothService::openView.isInitialized && openView.parent != null) {
                                        windowManager.removeViewImmediate(openView)
                                    }
                                }
                            }
                            .addOnFailureListener { _ ->
                            }
                    }

                    override fun onFailure(errorCode: Int) {
                    }
                })
            }
            overlayHandler.postDelayed(this, 1000)
        }
    }
}