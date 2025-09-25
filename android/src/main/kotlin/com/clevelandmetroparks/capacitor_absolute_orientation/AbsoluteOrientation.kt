package com.clevelandmetroparks.capacitor_absolute_orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import com.getcapacitor.Logger
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

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

            val quaternionArray = FloatArray(size = 4)
            SensorManager.getQuaternionFromVector(quaternionArray, event.values)
            val quaternion = Quaternion(
                x = quaternionArray[1],
                y = quaternionArray[2],
                z = quaternionArray[3],
                w = quaternionArray[0]
            )
            val reading = AbsoluteOrientationReading(
                timestamp,
                quaternion,
                quaternionToCompassHeading(quaternion)
            )
//            Logger.info("AbsoluteOrientation", "$alpha $beta $gamma")
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
                    sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_FASTEST
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
        val yaw = atan2(2.0 * (x * z - w * y), 1.0 - 2.0 * (y * y + z * z))
        var heading = toDegrees(-yaw)
        if (heading < 0) {
            heading += 360
        }
        return heading.toFloat()
    }

    private fun alphaBetaGammaToQuaternion(alpha: Float, beta: Float, gamma: Float): Quaternion {
        val x = toRadians(beta.toDouble())
        val y = toRadians(alpha.toDouble())
        val z = -toRadians(gamma.toDouble())

        val cX = cos(x / 2.0)
        val cY = cos(y / 2.0)
        val cZ = cos(z / 2.0)

        val sX = sin(x / 2.0)
        val sY = sin(y / 2.0)
        val sZ = sin(z / 2.0)

        val qX = sX * cY * cZ + cX * sY * sZ
        val qY = cX * sY * cZ - sX * cY * sZ
        val qZ = cX * cY * sZ - sX * sY * cZ
        val qW = cX * cY * cZ + sX * sY * sZ

        return Quaternion(qX.toFloat(), qY.toFloat(), qZ.toFloat(), qW.toFloat())
    }
}
