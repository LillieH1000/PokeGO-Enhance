package h.lillie.pokegotouch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val accessibilityPermissionButton: Button = findViewById(R.id.accessibilityPermissionButton)
        accessibilityPermissionButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val launchPokemonGOButton: Button = findViewById(R.id.launchPokemonGOButton)
        val pokemonGOIntent: Intent? = packageManager.getLaunchIntentForPackage("com.nianticlabs.pokemongo")
        if (pokemonGOIntent != null) {
            launchPokemonGOButton.visibility = View.VISIBLE
        }
        launchPokemonGOButton.setOnClickListener {
            if (pokemonGOIntent != null) {
                startActivity(pokemonGOIntent)
            }
        }
    }
}