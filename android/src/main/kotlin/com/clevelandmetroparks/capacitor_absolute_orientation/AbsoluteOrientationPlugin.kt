package com.clevelandmetroparks.capacitor_absolute_orientation

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@Suppress("unused")
@CapacitorPlugin(name = "AbsoluteOrientation")
class AbsoluteOrientationPlugin : Plugin() {
    private var implementation: AbsoluteOrientation? = null
    private val readingListeners: MutableMap<String, PluginCall> = mutableMapOf()

    override fun load() {
        implementation = AbsoluteOrientation(context, onReading = ::onReading)
    }

    private fun onReading(reading: AbsoluteOrientationReading) {
        val obj = reading.toJSObject()
        readingListeners.forEach { listener ->
            listener.value.setKeepAlive(true)
            listener.value.resolve(obj)
        }
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    fun start(call: PluginCall) {
        implementation?.start()
        call.resolve()
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    fun stop(call: PluginCall) {
        implementation?.stop()
        call.resolve()
    }

    @PluginMethod()
    fun isActivated(call: PluginCall) {
        val value = implementation?.isActivated()
        val ret = JSObject()
        ret.put("value", value ?: false)
        call.resolve(ret)
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    fun addReadingListener(call: PluginCall) {
        readingListeners.put(call.callbackId, call)
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    fun removeReadingListener(call: PluginCall) {
        val id = call.getString("id")
        if (id == null) {
            return
        }
        readingListeners.remove(id)?.release(bridge)
    }

}
