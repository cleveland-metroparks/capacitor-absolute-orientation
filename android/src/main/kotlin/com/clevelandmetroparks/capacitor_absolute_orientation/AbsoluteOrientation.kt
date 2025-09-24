package com.clevelandmetroparks.capacitor_absolute_orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock

class AbsoluteOrientation(
    private val context: Context, val onReading: (reading: Reading) -> Unit
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
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
            val alpha = if (azimuth < 0) azimuth + 360f else azimuth
            val beta = pitch
            val gamma = roll
            val reading = Reading(timestamp, quaternion, alpha, beta, gamma)
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
}
