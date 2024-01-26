package h.lillie.pokegotouch

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("ClickableViewAccessibility","InflateParams")
class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (!Settings.canDrawOverlays(this@Main)) {
            startActivityForResult(Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ), 0)
        } else {
            val gestureDetector = GestureDetector(this@Main, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    Log.d("Touch x", e.x.toString())
                    Log.d("Touch y", e.y.toString())
                    return true
                }
            })

            val view = View(this@Main)
            view.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event!!)
            }

            val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            val windowManagerLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            )
            windowManagerLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            windowManagerLayoutParams.format = PixelFormat.TRANSLUCENT
            windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            windowManagerLayoutParams.gravity = Gravity.TOP

            windowManager.addView(view, windowManagerLayoutParams)
        }
    }
}