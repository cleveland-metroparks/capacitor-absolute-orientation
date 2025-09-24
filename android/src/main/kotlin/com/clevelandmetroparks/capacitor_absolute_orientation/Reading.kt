package com.clevelandmetroparks.capacitor_absolute_orientation

class Reading(
    var timestamp: Long,
    var quaternion: Quaternion,
    var alpha: Float,
    var beta: Float,
    var gamma: Float
)