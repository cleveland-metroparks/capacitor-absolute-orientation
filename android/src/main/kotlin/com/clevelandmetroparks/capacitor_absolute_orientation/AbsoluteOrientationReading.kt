package com.clevelandmetroparks.capacitor_absolute_orientation

import com.getcapacitor.JSObject

class AbsoluteOrientationReading(
    var timestamp: Long,
    var quaternion: Quaternion?,
    var compassHeading: Float
) {
    fun toJSObject(): JSObject {
        val obj = JSObject()
        obj.put("timestamp", timestamp)
        quaternion?.let {
            obj.put("quaternion", it.toJSObject())
        }
        obj.put("compassHeading", compassHeading)
        return obj
    }
}