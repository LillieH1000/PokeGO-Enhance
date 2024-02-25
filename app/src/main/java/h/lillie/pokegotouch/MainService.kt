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
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.util.DisplayMetrics
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetTextI18n")
class MainService : AccessibilityService() {
    private lateinit var windowManager: WindowManager
    private lateinit var scope: Job

    private lateinit var mainRelativeLayout: RelativeLayout
    private lateinit var mainLinearLayout: LinearLayout
    private lateinit var startButton: Button

    override fun onServiceConnected() {
        super.onServiceConnected()

        // Notification

        showNotification()

        // Set Window Manager

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Open View

        val mainLayoutGradient = GradientDrawable()
        mainLayoutGradient.cornerRadii = floatArrayOf(60f, 60f, 0f, 0f, 0f, 0f, 60f, 60f)
        mainLayoutGradient.shape = GradientDrawable.RECTANGLE
        mainLayoutGradient.setColor(getColor(R.color.grey))

        mainRelativeLayout = RelativeLayout(this@MainService)
        mainRelativeLayout.background = mainLayoutGradient

        val mainLayoutText = TextView(this@MainService)
        mainLayoutText.gravity = Gravity.CENTER
        mainLayoutText.height = dpToPx(40)
        mainLayoutText.width = dpToPx(60)
        mainLayoutText.x = dpToPxF(10)
        mainLayoutText.y = dpToPxF(5)
        mainLayoutText.text = "PokeGo Touch"
        mainLayoutText.setTextColor(getColor(R.color.white))
        mainLayoutText.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)

        mainRelativeLayout.addView(mainLayoutText)
        mainRelativeLayout.setOnClickListener {
            if (this@MainService::scope.isInitialized && scope.isActive) {
                scope.cancel()
                Toast.makeText(this@MainService, "Stopped", Toast.LENGTH_SHORT).show()
            } else if (this@MainService::mainLinearLayout.isInitialized && mainLinearLayout.parent != null) {
                windowManager.removeViewImmediate(mainLinearLayout)
            } else {
                val mainGradientDrawable = GradientDrawable()
                mainGradientDrawable.cornerRadii = floatArrayOf(30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f)
                mainGradientDrawable.shape = GradientDrawable.RECTANGLE
                mainGradientDrawable.setColor(getColor(R.color.grey))

                mainLinearLayout = LinearLayout(this@MainService)
                mainLinearLayout.layoutParams = LinearLayout.LayoutParams(dpToPx(400), dpToPx(170))
                mainLinearLayout.orientation = LinearLayout.VERTICAL
                mainLinearLayout.background = mainGradientDrawable

                val optionText = TextView(this@MainService)
                optionText.height = dpToPx(40)
                optionText.width = dpToPx(400)
                optionText.gravity = Gravity.CENTER
                optionText.text = "Please select an option"
                optionText.setTextColor(getColor(R.color.white))

                val optionEdit = EditText(this@MainService)
                optionEdit.height = dpToPx(50)
                optionEdit.width = dpToPx(400)
                optionEdit.inputType = InputType.TYPE_CLASS_TEXT
                optionEdit.hint = "Enter amount"
                optionEdit.setHintTextColor(getColor(R.color.white))
                optionEdit.setTextColor(getColor(R.color.white))
                optionEdit.visibility = View.GONE

                val sendGiftsLinearLayout = LinearLayout(this@MainService)
                sendGiftsLinearLayout.layoutParams = LinearLayout.LayoutParams(dpToPx(400), dpToPx(50))
                sendGiftsLinearLayout.orientation = LinearLayout.HORIZONTAL
                sendGiftsLinearLayout.background = mainGradientDrawable
                sendGiftsLinearLayout.visibility = View.GONE

                val sendGiftsText = TextView(this@MainService)
                sendGiftsText.height = dpToPx(50)
                sendGiftsText.width = dpToPx(340)
                sendGiftsText.text = "Does the top of the list have gifts?"
                sendGiftsText.setTextColor(getColor(R.color.white))

                val sendGiftsSwitch = SwitchMaterial(ContextThemeWrapper(this@MainService, R.style.MainTheme))

                sendGiftsLinearLayout.addView(sendGiftsText)
                sendGiftsLinearLayout.addView(sendGiftsSwitch)

                val group = MaterialButtonToggleGroup(ContextThemeWrapper(this@MainService, R.style.MainTheme))
                group.layoutParams = ViewGroup.LayoutParams(dpToPx(400), dpToPx(80))
                group.isSelectionRequired = true
                group.isSingleSelection = true

                val sendMaterialButton = MaterialButton(ContextThemeWrapper(this@MainService, R.style.MainTheme), null, com.google.android.material.R.attr.materialButtonOutlinedStyle)
                sendMaterialButton.width = dpToPx(200)
                sendMaterialButton.text = "Send Gifts"
                sendMaterialButton.isAllCaps = false
                sendMaterialButton.setTextColor(getColor(R.color.white))
                sendMaterialButton.setOnClickListener {
                    addView(mainLinearLayout, 2, 280, 400, true)
                    optionText.text = "How many gifts would you like to send?"
                    optionEdit.visibility = View.VISIBLE
                    sendGiftsLinearLayout.visibility = View.VISIBLE
                    startButton.visibility = View.VISIBLE
                }

                val openMaterialButton = MaterialButton(ContextThemeWrapper(this@MainService, R.style.MainTheme), null, com.google.android.material.R.attr.materialButtonOutlinedStyle)
                openMaterialButton.width = dpToPx(200)
                openMaterialButton.text = "Open Gifts"
                openMaterialButton.isAllCaps = false
                openMaterialButton.setTextColor(getColor(R.color.white))
                openMaterialButton.setOnClickListener {
                    addView(mainLinearLayout, 2, 240, 400, true)
                    optionText.text = "How many gifts would you like to open?"
                    optionEdit.visibility = View.VISIBLE
                    sendGiftsLinearLayout.visibility = View.GONE
                    startButton.visibility = View.VISIBLE
                }

                group.addView(sendMaterialButton)
                group.addView(openMaterialButton)

                startButton = Button(this@MainService)
                startButton.height = dpToPx(50)
                startButton.width = dpToPx(400)
                startButton.text = "Start"
                startButton.visibility = View.GONE
                startButton.setOnClickListener {
                    val limit: Int? = optionEdit.text.toString().toIntOrNull()
                    var option: Int? = null
                    if (sendMaterialButton.isChecked) {
                        option = 0
                    }
                    if (openMaterialButton.isChecked) {
                        option = 1
                    }
                    if (limit != null && limit > 0 && option != null) {
                        var count = 0
                        val switchChecked: Boolean = sendGiftsSwitch.isChecked
                        if (this@MainService::mainLinearLayout.isInitialized && mainLinearLayout.parent != null) {
                            windowManager.removeViewImmediate(mainLinearLayout)
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
                                if (option == 0) {
                                    open(688f, 939f)
                                }
                                if (option == 1) {
                                    send(688f, 939f)
                                }
                                count += 1
                                if (count == limit) {
                                    scope.cancel()
                                }

                                // Friend 2
                                if (option == 0) {
                                    open(526f, 1296f)
                                }
                                if (option == 1) {
                                    send(526f, 1296f)
                                }
                                count += 1
                                if (count == limit) {
                                    scope.cancel()
                                }

                                // Friend 3
                                if (option == 0) {
                                    open(602f, 1604f)
                                }
                                if (option == 1) {
                                    send(602f, 1604f)
                                }
                                count += 1
                                if (count == limit) {
                                    scope.cancel()
                                }

                                // Friend 4
                                if (option == 0) {
                                    open(569f, 1991f)
                                }
                                if (option == 1) {
                                    send(569f, 1991f)
                                }
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

                mainLinearLayout.addView(group)
                mainLinearLayout.addView(optionText)
                mainLinearLayout.addView(optionEdit)
                mainLinearLayout.addView(sendGiftsLinearLayout)
                mainLinearLayout.addView(startButton)

                addView(mainLinearLayout, 2, 150, 400, false)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
    }

    private fun addView(view: View, align: Int?, height: Int?, width: Int?, update: Boolean) {
        val windowManagerLayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
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

        if (!update) {
            windowManager.addView(view, windowManagerLayoutParams)
        } else {
            windowManager.updateViewLayout(view, windowManagerLayoutParams)
        }
    }

    private fun dpToPx(dp: Int) : Int {
        return dp * (this@MainService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun dpToPxF(dp: Int) : Float {
        return dp * (this@MainService.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun showNotification() {
        val notificationChannel = NotificationChannel("PokeGOTouchNotificationChannelID", "PokeGOTouchNotificationChannel", NotificationManager.IMPORTANCE_MIN)
        notificationChannel.description = "PokeGO Touch channel for foreground service notification"

        val notificationManager = this@MainService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notification: Notification = NotificationCompat.Builder(this@MainService, "PokeGOTouchNotificationChannelID")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setSilent(true)
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

        notificationManager.notify(1, notification)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == "showOverlay") {
                    if (this@MainService::scope.isInitialized && scope.isActive) {
                        scope.cancel()
                    }
                    if (this@MainService::mainRelativeLayout.isInitialized && mainRelativeLayout.parent == null) {
                        addView(mainRelativeLayout, 1, 50, 80, false)
                    }
                }

                if (intent.action == "hideOverlay") {
                    if (this@MainService::scope.isInitialized && scope.isActive) {
                        scope.cancel()
                    }
                    if (this@MainService::mainRelativeLayout.isInitialized && mainRelativeLayout.parent != null) {
                        windowManager.removeViewImmediate(mainRelativeLayout)
                    }
                    if (this@MainService::mainLinearLayout.isInitialized && mainLinearLayout.parent != null) {
                        windowManager.removeViewImmediate(mainLinearLayout)
                    }
                }

                if (intent.action == "notificationDismissed") {
                    showNotification()
                }
            }
        }
    }
}