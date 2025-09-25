package com.clevelandmetroparks.capacitor_absolute_orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlin.math.atan2

class AbsoluteOrientation(
    private val context: Context, val onReading: (reading: AbsoluteOrientationReading) -> Unit
) {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private var started = false

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type != Sensor.TYPE_ROTATION_VECTOR) {
                return
            }
            val timestamp =
                System.currentTimeMillis() - ((SystemClock.elapsedRealtimeNanos() - event.timestamp) / 1_000_000)
            val quaternion =
                Quaternion(event.values[0], event.values[1], event.values[2], event.values[3])
            val reading = AbsoluteOrientationReading(
                timestamp,
                quaternion,
                quaternionToCompassHeading(quaternion)
            )
            onReading(reading)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun start() {
        if (started) {
            return
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also { rotationSensor ->
            if (sensorManager.registerListener(
                    sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL
                )
            ) {
                started = true
            }
        }
    }

    fun stop() {
        if (!started) {
            return
        }
        sensorManager.unregisterListener(sensorEventListener)
    }

    fun isActivated(): Boolean {
        return started
    }

    private fun quaternionToCompassHeading(quaternion: Quaternion): Float {
        val x = quaternion.x
        val y = quaternion.y
        val z = quaternion.z
        val w = quaternion.w
        val yaw = atan2(2.0 * (w * z + x * y), 1.0 - 2.0 * (y * y + z * z))
        var heading = Math.toDegrees(-yaw)
        if (heading < 0) {
            heading += 360
        }
        return heading.toFloat()
    }
}
