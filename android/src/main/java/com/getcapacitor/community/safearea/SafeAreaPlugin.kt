package com.getcapacitor.community.safearea

import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "SafeArea")
class SafeAreaPlugin : Plugin() {
    private var implementation: SafeArea? = null

    override fun load() {
        implementation = SafeArea(activity, bridge.webView)

        val enabled = config.configJSON.optBoolean("enabled", false)

        if (enabled) {
            implementation?.offset = config.configJSON.optInt("offset", 0)
            implementation?.enable(false, AppearanceConfig(config.configJSON))
        }
    }

    override fun handleOnPause() {
        implementation?.resetDecorFitsSystemWindows()
        super.handleOnPause()
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    fun enable(call: PluginCall) {
        val jsonObject = call.getObject("config")

        if (jsonObject.has("offset")) {
            implementation?.offset = jsonObject.optInt("offset", 0)
        }

        val appearanceConfig = AppearanceConfig(jsonObject)
        implementation?.enable(true, appearanceConfig)

        call.resolve()
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    fun disable(call: PluginCall) {
        val appearanceConfig = AppearanceConfig(call.getObject("config"))
        implementation?.disable(appearanceConfig)

        call.resolve()
    }
}
