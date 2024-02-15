package h.lillie.pokegotouch

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.text.InputType
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetTextI18n")
class MainService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var scope: Job

    private lateinit var openLayout: RelativeLayout
    private lateinit var openPopup: RelativeLayout

    private lateinit var sendLayout: RelativeLayout

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        // Notification

        val notificationChannel = NotificationChannel("PokeGOTouchNotificationChannelID", "PokeGOTouchNotificationChannel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.description = "PokeGO Touch channel for foreground service notification"

        val notificationManager = this@MainService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notification: Notification = NotificationCompat.Builder(this@MainService, "PokeGOTouchNotificationChannelID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("PokeGO Touch")
            .setContentText("Running")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Foreground And Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }

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
                    if (!Python.isStarted()) {
                        Python.start(AndroidPlatform(this@MainService))
                    }

                    val py = Python.getInstance()
                    val module = py.getModule("control")
                    module.callAttr("runStop")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainService, "Stopped", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val openPopupGradient = GradientDrawable()
                openPopupGradient.cornerRadii = floatArrayOf(30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f)
                openPopupGradient.shape = GradientDrawable.RECTANGLE
                openPopupGradient.setColor(getColor(R.color.grey))

                openPopup = RelativeLayout(this@MainService)
                openPopup.background = openPopupGradient

                val openText = TextView(this@MainService)
                openText.height = dpToPx(50)
                openText.width = dpToPx(400)
                openText.text = "How many gifts would you like to open?"
                openText.setTextColor(getColor(R.color.white))

                val openEdit = EditText(this@MainService)
                openEdit.height = dpToPx(50)
                openEdit.width = dpToPx(400)
                openEdit.y = dpToPxF(60)
                openEdit.inputType = InputType.TYPE_CLASS_TEXT
                openEdit.hint = "Enter amount"
                openEdit.setHintTextColor(getColor(R.color.white))
                openEdit.setTextColor(getColor(R.color.white))

                val openButton = Button(this@MainService)
                openButton.height = dpToPx(50)
                openButton.width = dpToPx(400)
                openButton.y = dpToPxF(120)
                openButton.text = "Start"
                openButton.setOnClickListener {
                    val limit: Int? = openEdit.text.toString().toIntOrNull()
                    if (limit != null) {
                        if (this@MainService::openPopup.isInitialized && openPopup.windowToken != null) {
                            windowManager.removeViewImmediate(openPopup)
                        }
                        scope = CoroutineScope(Dispatchers.Default).launch {
                            if (!Python.isStarted()) {
                                Python.start(AndroidPlatform(this@MainService))
                            }

                            val py = Python.getInstance()
                            val module = py.getModule("control")
                            module.callAttr("runConnect", filesDir)
                            module.callAttr("runOpenThread", limit)
                        }
                    }
                }

                openPopup.addView(openText)
                openPopup.addView(openEdit)
                openPopup.addView(openButton)

                addView(openPopup, 2, 400, 400, null, null)
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
                    if (!Python.isStarted()) {
                        Python.start(AndroidPlatform(this@MainService))
                    }

                    val py = Python.getInstance()
                    val module = py.getModule("control")
                    module.callAttr("runStop")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainService, "Stopped", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@MainService, "Sending In Development", Toast.LENGTH_SHORT).show()
            }
        }

        // Add Views

        addView(openLayout, 1, 60, 80, null, 50)
        addView(sendLayout, 0, 60, 80, null, 50)

        // Launch Pokemon GO

        val pokemonGOIntent: Intent? = packageManager.getLaunchIntentForPackage("com.nianticlabs.pokemongo")
        if (pokemonGOIntent != null) {
            startActivity(pokemonGOIntent)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this@MainService::openLayout.isInitialized && openLayout.windowToken != null) {
            windowManager.removeViewImmediate(openLayout)
        }
        if (this@MainService::openPopup.isInitialized && openPopup.windowToken != null) {
            windowManager.removeViewImmediate(openPopup)
        }
        if (this@MainService::sendLayout.isInitialized && sendLayout.windowToken != null) {
            windowManager.removeViewImmediate(sendLayout)
        }
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

    private fun dpToPxF(dp: Int) : Float {
        return dp * (this@MainService.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}