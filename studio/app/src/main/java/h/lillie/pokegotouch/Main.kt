package h.lillie.pokegotouch

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class Main : AppCompatActivity() {
    @Suppress("Deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (!Settings.canDrawOverlays(this@Main)) {
            startActivityForResult(Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ), 0)
        } else {
            val view = View(this@Main)
            view.setBackgroundColor(Color.RED)
            view.setOnClickListener {
                if (!Python.isStarted()) {
                    Python.start(AndroidPlatform(this@Main))
                }

                val wifiManager = this@Main.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val ipAddress = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

                val py = Python.getInstance()
                val module = py.getModule("control")
                module.callAttr("run", ipAddress, filesDir)
            }

            val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            val windowManagerLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            )
            windowManagerLayoutParams.height = 100
            windowManagerLayoutParams.width = 100
            windowManagerLayoutParams.format = PixelFormat.TRANSLUCENT
            windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            windowManagerLayoutParams.gravity = Gravity.TOP
            windowManager.addView(view, windowManagerLayoutParams)

            val intent: Intent? = packageManager.getLaunchIntentForPackage("com.nianticlabs.pokemongo")
            if (intent != null) {
                startActivity(intent)
            }
        }
    }
}