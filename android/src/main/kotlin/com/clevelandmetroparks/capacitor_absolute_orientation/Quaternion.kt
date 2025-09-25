package com.clevelandmetroparks.capacitor_absolute_orientation

import com.getcapacitor.JSObject

class Quaternion(
    var x: Float,
    var y: Float,
    var z: Float,
    var w: Float
) {
    fun toJSObject(): JSObject {
        val obj = JSObject()
        obj.put("x", x)
        obj.put("y", y)
        obj.put("z", z)
        obj.put("w", w)
        return obj
    }
}