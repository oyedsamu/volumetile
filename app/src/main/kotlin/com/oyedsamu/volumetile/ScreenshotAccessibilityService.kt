package com.oyedsamu.volumetile

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ScreenshotAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {
        var instance: ScreenshotAccessibilityService? = null
    }
}
