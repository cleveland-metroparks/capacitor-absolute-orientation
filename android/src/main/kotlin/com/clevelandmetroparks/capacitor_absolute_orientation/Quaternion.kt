package com.clevelandmetroparks.capacitor_absolute_orientation

class Quaternion(
    var x: Float,
    var y: Float,
    var z: Float,
    var w: Float
) {
    fun asArray(): FloatArray {
        return floatArrayOf(x, y, z, w)
    }
}