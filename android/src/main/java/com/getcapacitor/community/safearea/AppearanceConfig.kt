package com.getcapacitor.community.safearea

import org.json.JSONObject

class AppearanceConfig(fromJSONObject: JSONObject? = null) {
    var customColorsForSystemBars: Boolean = true
    var statusBarColor: String = "#000000"
    var statusBarContent: String = "light"
    var navigationBarColor: String = "#000000"
    var navigationBarContent: String = "light"

    init {
        if (fromJSONObject != null) {
            if (fromJSONObject.has("customColorsForSystemBars")) {
                customColorsForSystemBars =
                    fromJSONObject.optBoolean("customColorsForSystemBars", true)
            }
            if (fromJSONObject.has("statusBarColor")) {
                statusBarColor = fromJSONObject.optString("statusBarColor", "#000000")
            }
            if (fromJSONObject.has("statusBarContent")) {
                statusBarContent = fromJSONObject.optString("statusBarContent", "light")
            }
            if (fromJSONObject.has("navigationBarColor")) {
                navigationBarColor = fromJSONObject.optString("navigationBarColor", "#000000")
            }
            if (fromJSONObject.has("navigationBarContent")) {
                navigationBarContent = fromJSONObject.optString("navigationBarContent", "light")
            }
        }
    }
}
