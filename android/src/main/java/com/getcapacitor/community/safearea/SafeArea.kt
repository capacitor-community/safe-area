package com.getcapacitor.community.safearea

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import android.webkit.WebView
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class SafeArea(private val activity: Activity, private val webView: WebView) {
    var offset = 0

    fun enable(updateInsets: Boolean, appearanceConfig: AppearanceConfig) {
        activity.window.decorView.getRootView().setOnApplyWindowInsetsListener { view, insets ->
            updateInsets()
            view.onApplyWindowInsets(insets)
        }

        updateAppearance(appearanceConfig)

        if (updateInsets) {
            updateInsets()
        }
    }

    fun disable(appearanceConfig: AppearanceConfig) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)
        activity.window.decorView.getRootView().setOnApplyWindowInsetsListener(null)
        resetProperties()

        updateAppearance(appearanceConfig)
    }

    private fun updateAppearance(appearanceConfig: AppearanceConfig) {
        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsControllerCompat.isAppearanceLightStatusBars =
            appearanceConfig.statusBarContent == "dark"
        windowInsetsControllerCompat.isAppearanceLightNavigationBars =
            appearanceConfig.navigationBarContent == "dark"

        val window = activity.window

        if (appearanceConfig.customColorsForSystemBars) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(appearanceConfig.statusBarColor)
            window.navigationBarColor = Color.parseColor(appearanceConfig.navigationBarColor)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    private fun updateInsets() {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        val windowInsets = ViewCompat.getRootWindowInsets(activity.window.decorView)
        val systemBarsInsets =
            windowInsets?.getInsets(WindowInsetsCompat.Type.systemBars()) ?: Insets.NONE
        val imeInsets = windowInsets?.getInsets(WindowInsetsCompat.Type.ime()) ?: Insets.NONE

        val density = activity.resources.displayMetrics.density

        setProperty("top", Math.round(systemBarsInsets.top / density) + offset)
        setProperty("left", Math.round(systemBarsInsets.left / density))
        if (imeInsets.bottom > 0) {
            setProperty("bottom", 0)
        } else {
            setProperty("bottom", Math.round(systemBarsInsets.bottom / density) + offset)
        }
        setProperty("right", Math.round(systemBarsInsets.right / density))

        activity.window.decorView.setPadding(0, 0, 0, imeInsets.bottom)
    }

    private fun resetProperties() {
        setProperty("top", 0)
        setProperty("left", 0)
        setProperty("bottom", 0)
        setProperty("right", 0)
    }

    private fun setProperty(position: String, size: Int) {
        webView.loadUrl("javascript:document.querySelector(':root')?.style.setProperty('--safe-area-inset-" + position + "', 'max(env(safe-area-inset-" + position + "), " + size + "px)');void(0);")
    }
}
