package h.lillie.pokegotouch

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputType
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetTextI18n", "UseSwitchCompatOrMaterialCode")
class MainService : AccessibilityService() {
    private lateinit var windowManager: WindowManager
    private lateinit var scope: Job

    private lateinit var openLayout: RelativeLayout
    private lateinit var openPopup: LinearLayout
    private lateinit var sendLayout: RelativeLayout
    private lateinit var sendPopup: LinearLayout

    override fun onCreate() {
        super.onCreate()

        // Notification

        showNotification()

        // Set Window Manager

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Open View

        val openLayoutGradient = GradientDrawable()
        openLayoutGradient.cornerRadii = floatArrayOf(60f, 60f, 0f, 0f, 0f, 0f, 60f, 60f)
        openLayoutGradient.shape = GradientDrawable.RECTANGLE
        openLayoutGradient.setColor(getColor(R.color.grey))

        openLayout = RelativeLayout(this@MainService)
        openLayout.background = openLayoutGradient

        val openLayoutText = TextView(this@MainService)
        openLayoutText.gravity = Gravity.CENTER or Gravity.RIGHT
        openLayoutText.height = dpToPx(60)
        openLayoutText.width = dpToPx(60)
        openLayoutText.text = "Open"
        openLayoutText.setTextColor(getColor(R.color.white))

        openLayout.addView(openLayoutText)
        openLayout.setOnClickListener {
            if (this@MainService::scope.isInitialized && scope.isActive) {
                scope.cancel()
                CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainService, "Stopped", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (this@MainService::openPopup.isInitialized && openPopup.parent != null) {
                windowManager.removeViewImmediate(openPopup)
            } else if (this@MainService::sendPopup.isInitialized && sendPopup.parent != null) {
                windowManager.removeViewImmediate(sendPopup)
            } else {
                val openPopupGradient = GradientDrawable()
                openPopupGradient.cornerRadii = floatArrayOf(30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f)
                openPopupGradient.shape = GradientDrawable.RECTANGLE
                openPopupGradient.setColor(getColor(R.color.grey))

                openPopup = LinearLayout(this@MainService)
                openPopup.orientation = LinearLayout.VERTICAL
                openPopup.background = openPopupGradient

                val openText = TextView(this@MainService)
                openText.height = dpToPx(50)
                openText.width = dpToPx(400)
                openText.gravity = Gravity.CENTER
                openText.text = "How many gifts would you like to open?"
                openText.setTextColor(getColor(R.color.white))

                val openEdit = EditText(this@MainService)
                openEdit.height = dpToPx(50)
                openEdit.width = dpToPx(400)
                openEdit.inputType = InputType.TYPE_CLASS_TEXT
                openEdit.hint = "Enter amount"
                openEdit.setHintTextColor(getColor(R.color.white))
                openEdit.setTextColor(getColor(R.color.white))

                val openButton = Button(this@MainService)
                openButton.height = dpToPx(50)
                openButton.width = dpToPx(400)
                openButton.text = "Start"
                openButton.setOnClickListener {
                    val limit: Int? = openEdit.text.toString().toIntOrNull()
                    if (limit != null && limit > 0) {
                        var openLimit = 0
                        if (this@MainService::openPopup.isInitialized && openPopup.parent != null) {
                            windowManager.removeViewImmediate(openPopup)
                        }
                        scope = CoroutineScope(Dispatchers.Default).launch {
                            while (true) {
                                ensureActive()
                                suspend fun open(x: Float, y: Float) = withContext(Dispatchers.Default) {
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
                                open(688f, 939f)
                                openLimit += 1
                                if (openLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 2
                                open(526f, 1296f)
                                openLimit += 1
                                if (openLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 3
                                open(602f, 1604f)
                                openLimit += 1
                                if (openLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 4
                                open(569f, 1991f)
                                openLimit += 1
                                if (openLimit == limit) {
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

                openPopup.addView(openText)
                openPopup.addView(openEdit)
                openPopup.addView(openButton)

                addView(openPopup, 2, 170, 400, null, null)
            }
        }

        // Send View

        val sendLayoutGradient = GradientDrawable()
        sendLayoutGradient.cornerRadii = floatArrayOf(0f, 0f, 60f, 60f, 60f, 60f, 0f, 0f)
        sendLayoutGradient.shape = GradientDrawable.RECTANGLE
        sendLayoutGradient.setColor(getColor(R.color.grey))

        sendLayout = RelativeLayout(this@MainService)
        sendLayout.background = sendLayoutGradient

        val sendLayoutText = TextView(this@MainService)
        sendLayoutText.gravity = Gravity.CENTER
        sendLayoutText.height = dpToPx(60)
        sendLayoutText.width = dpToPx(60)
        sendLayoutText.text = "Send"
        sendLayoutText.setTextColor(getColor(R.color.white))

        sendLayout.addView(sendLayoutText)
        sendLayout.setOnClickListener {
            if (this@MainService::scope.isInitialized && scope.isActive) {
                scope.cancel()
                CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainService, "Stopped", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (this@MainService::openPopup.isInitialized && openPopup.parent != null) {
                windowManager.removeViewImmediate(openPopup)
            } else if (this@MainService::sendPopup.isInitialized && sendPopup.parent != null) {
                windowManager.removeViewImmediate(sendPopup)
            } else {
                val sendPopupGradient = GradientDrawable()
                sendPopupGradient.cornerRadii = floatArrayOf(30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f)
                sendPopupGradient.shape = GradientDrawable.RECTANGLE
                sendPopupGradient.setColor(getColor(R.color.grey))

                sendPopup = LinearLayout(this@MainService)
                sendPopup.orientation = LinearLayout.VERTICAL
                sendPopup.background = sendPopupGradient

                val sendText = TextView(this@MainService)
                sendText.height = dpToPx(50)
                sendText.width = dpToPx(400)
                sendText.gravity = Gravity.CENTER
                sendText.text = "How many gifts would you like to send?"
                sendText.setTextColor(getColor(R.color.white))

                val sendEdit = EditText(this@MainService)
                sendEdit.height = dpToPx(50)
                sendEdit.width = dpToPx(400)
                sendEdit.inputType = InputType.TYPE_CLASS_TEXT
                sendEdit.hint = "Enter amount"
                sendEdit.setHintTextColor(getColor(R.color.white))
                sendEdit.setTextColor(getColor(R.color.white))

                val sendGiftsText = TextView(this@MainService)
                sendGiftsText.height = dpToPx(50)
                sendGiftsText.width = dpToPx(400)
                sendGiftsText.gravity = Gravity.CENTER
                sendGiftsText.text = "Does the top of the list have gifts?"
                sendGiftsText.setTextColor(getColor(R.color.white))

                val sendGiftsSwitch = Switch(this@MainService)

                val sendButton = Button(this@MainService)
                sendButton.height = dpToPx(50)
                sendButton.width = dpToPx(400)
                sendButton.text = "Start"
                sendButton.setOnClickListener {
                    val limit: Int? = sendEdit.text.toString().toIntOrNull()
                    if (limit != null && limit > 0) {
                        var sendLimit = 0
                        val switchChecked: Boolean = sendGiftsSwitch.isChecked
                        if (this@MainService::sendPopup.isInitialized && sendPopup.parent != null) {
                            windowManager.removeViewImmediate(sendPopup)
                        }
                        scope = CoroutineScope(Dispatchers.Default).launch {
                            while (true) {
                                ensureActive()
                                suspend fun send(x: Float, y: Float) = withContext(Dispatchers.Default) {
                                    val path = Path()

                                    path.reset()
                                    path.moveTo(x, y)
                                    dispatchGesture(GestureDescription.Builder()
                                        .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
                                        .build(), null, null)
                                    delay(3000)

                                    if (switchChecked) {
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
                                send(688f, 939f)
                                sendLimit += 1
                                if (sendLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 2
                                send(526f, 1296f)
                                sendLimit += 1
                                if (sendLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 3
                                send(602f, 1604f)
                                sendLimit += 1
                                if (sendLimit == limit) {
                                    scope.cancel()
                                }

                                // Friend 4
                                send(569f, 1991f)
                                sendLimit += 1
                                if (sendLimit == limit) {
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

                sendPopup.addView(sendText)
                sendPopup.addView(sendEdit)
                sendPopup.addView(sendGiftsText)
                sendPopup.addView(sendGiftsSwitch)
                sendPopup.addView(sendButton)

                addView(sendPopup, 2, 270, 400, null, null)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
    }

    private fun addView(view: View, align: Int?, height: Int?, width: Int?, x: Int?, y: Int?) {
        val windowManagerLayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        windowManagerLayoutParams.format = PixelFormat.TRANSLUCENT

        if (align != null) {
            if (align == 0) {
                windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                windowManagerLayoutParams.gravity = Gravity.TOP or Gravity.LEFT
            }
            if (align == 1) {
                windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                windowManagerLayoutParams.gravity = Gravity.TOP or Gravity.RIGHT
            }
            if (align == 2) {
                windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                windowManagerLayoutParams.gravity = Gravity.CENTER
            }
        }

        if (height != null) {
            windowManagerLayoutParams.height = dpToPx(height)
        }
        if (width != null) {
            windowManagerLayoutParams.width = dpToPx(width)
        }
        if (x != null) {
            windowManagerLayoutParams.x = dpToPx(x)
        }
        if (y != null) {
            windowManagerLayoutParams.y = dpToPx(y)
        }

        windowManager.addView(view, windowManagerLayoutParams)
    }

    private fun dpToPx(dp: Int) : Int {
        return dp * (this@MainService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun showNotification() {
        val notificationChannel = NotificationChannel("PokeGOTouchNotificationChannelID", "PokeGOTouchNotificationChannel", NotificationManager.IMPORTANCE_NONE)
        notificationChannel.description = "PokeGO Touch channel for foreground service notification"

        val notificationManager = this@MainService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notification: Notification = NotificationCompat.Builder(this@MainService, "PokeGOTouchNotificationChannelID")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("PokeGO Touch")
            .addAction(0, "Show Overlay",
                PendingIntent.getBroadcast(this@MainService,
                    1,
                    Intent().setAction("showOverlay"),
                    PendingIntent.FLAG_IMMUTABLE))
            .addAction(0, "Hide Overlay",
                PendingIntent.getBroadcast(this@MainService,
                    1,
                    Intent().setAction("hideOverlay"),
                    PendingIntent.FLAG_IMMUTABLE))
            .setDeleteIntent(PendingIntent.getBroadcast(this@MainService,
                1,
                Intent().setAction("notificationDismissed"),
                PendingIntent.FLAG_IMMUTABLE))
            .build()

        registerReceiver(broadcastReceiver, IntentFilter("showOverlay"), RECEIVER_NOT_EXPORTED)
        registerReceiver(broadcastReceiver, IntentFilter("hideOverlay"), RECEIVER_NOT_EXPORTED)
        registerReceiver(broadcastReceiver, IntentFilter("notificationDismissed"), RECEIVER_NOT_EXPORTED)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == "showOverlay") {
                    if (this@MainService::openLayout.isInitialized && openLayout.parent == null) {
                        addView(openLayout, 1, 60, 80, null, 50)
                    }
                    if (this@MainService::sendLayout.isInitialized && sendLayout.parent == null) {
                        addView(sendLayout, 0, 60, 80, null, 50)
                    }
                }

                if (intent.action == "hideOverlay") {
                    if (this@MainService::openLayout.isInitialized && openLayout.parent != null) {
                        windowManager.removeViewImmediate(openLayout)
                    }
                    if (this@MainService::openPopup.isInitialized && openPopup.parent != null) {
                        windowManager.removeViewImmediate(openPopup)
                    }
                    if (this@MainService::sendLayout.isInitialized && sendLayout.parent != null) {
                        windowManager.removeViewImmediate(sendLayout)
                    }
                    if (this@MainService::sendPopup.isInitialized && sendPopup.parent != null) {
                        windowManager.removeViewImmediate(sendPopup)
                    }
                }

                if (intent.action == "notificationDismissed") {
                    showNotification()
                }
            }
        }
    }
}