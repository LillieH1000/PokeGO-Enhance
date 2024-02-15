package h.lillie.pokegotouch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val notificationPermissionButton: Button = findViewById(R.id.notificationPermissionButton)
        notificationPermissionButton.setOnClickListener {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val displayOverOtherAppsPermissionButton: Button = findViewById(R.id.displayOverOtherAppsPermissionButton)
        displayOverOtherAppsPermissionButton.setOnClickListener {
            startActivity(Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ))
        }

        val usbDebuggingPermissionButton: Button = findViewById(R.id.usbDebuggingPermissionButton)
        usbDebuggingPermissionButton.setOnClickListener {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this@Main))
            }

            val py = Python.getInstance()
            val module = py.getModule("control")
            module.callAttr("runConnect", filesDir)
        }

        val launchPokemonGOButton: Button = findViewById(R.id.launchPokemonGOButton)
        val pokemonGOIntent: Intent? = packageManager.getLaunchIntentForPackage("com.nianticlabs.pokemongo")
        if (pokemonGOIntent != null) {
            launchPokemonGOButton.visibility = View.VISIBLE
        }
        launchPokemonGOButton.setOnClickListener {
            startForegroundService(Intent(this@Main, MainService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this@Main, MainService::class.java))
        finishAffinity()
    }
}