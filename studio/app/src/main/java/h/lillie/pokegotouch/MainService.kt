package h.lillie.pokegotouch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.content.res.ColorStateList
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainService : Service() {
    private lateinit var windowView: View
    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val windowManagerLayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        windowManagerLayoutParams.height = dpToPx(60)
        windowManagerLayoutParams.width = dpToPx(80)
        windowManagerLayoutParams.format = PixelFormat.TRANSLUCENT
        windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        windowManagerLayoutParams.gravity = Gravity.TOP or Gravity.RIGHT
        windowManagerLayoutParams.y = dpToPx(50)

        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadii = floatArrayOf(60f, 60f, 0f, 0f, 0f, 0f, 60f, 60f)
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.color = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(R.color.grey)
        )

        windowView = View(this@MainService)
        windowView.background = gradientDrawable
        windowView.setOnClickListener {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this@MainService))
            }

            val py = Python.getInstance()
            val module = py.getModule("control")
            module.callAttr("run", filesDir)
        }

        windowManager.addView(windowView, windowManagerLayoutParams)

        val intent: Intent? = packageManager.getLaunchIntentForPackage("com.nianticlabs.pokemongo")
        if (intent != null) {
            startActivity(intent)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeViewImmediate(windowView)
    }

    private fun dpToPx(dp: Int) : Int {
        return dp * (this@MainService.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }
}