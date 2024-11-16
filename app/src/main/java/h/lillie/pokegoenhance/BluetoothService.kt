package h.lillie.pokegoenhance

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BluetoothService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.contentChangeTypes != AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED || event.packageName == null || event.className == null) return

        if (event.packageName == "com.android.settings" && event.className == "com.android.settings.bluetooth.BluetoothPairingDialog") return

        // PokeGO Plus Auto Pair
        if (event.packageName == "com.android.settings" && Regex("[^A-Za-z0-9 ]").replace(event.text.toString(), "").contains("pair with pokemon go plus", true) && event.source != null) {
            val pairButtonList = event.source!!.findAccessibilityNodeInfosByViewId("android:id/button1")
            if (pairButtonList.isNotEmpty()) {
                pairButtonList[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    override fun onInterrupt() {
    }
}