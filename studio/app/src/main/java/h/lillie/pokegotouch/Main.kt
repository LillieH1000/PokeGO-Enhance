package h.lillie.pokegotouch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("ClickableViewAccessibility")
class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)

        val gestureDetector = GestureDetector(this@Main, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                Log.d("Touch x", e.x.toString())
                Log.d("Touch y", e.y.toString())
                return true
            }
        })

        relativeLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event!!)
        }
    }
}