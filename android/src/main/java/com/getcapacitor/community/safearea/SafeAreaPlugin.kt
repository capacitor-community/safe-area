package com.getcapacitor.community.safearea

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "SafeArea")
class SafeAreaPlugin : Plugin() {
    private val implementation = SafeArea()
    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")
        val ret = JSObject()
        ret.put("value", value?.let { implementation.echo(it) })
        call.resolve(ret)
    }
}
