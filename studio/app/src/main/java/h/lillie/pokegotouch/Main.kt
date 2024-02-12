package h.lillie.pokegotouch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this@Main, MainService::class.java))
        finishAffinity()
    }

    private fun checkPermissions() {
        if (!Settings.canDrawOverlays(this@Main)) {
            activityLauncher.launch(Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ))
        } else if (!NotificationManagerCompat.from(this@Main).areNotificationsEnabled()) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            startForegroundService(Intent(this@Main, MainService::class.java))
        }
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        checkPermissions()
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
        checkPermissions()
    }
}