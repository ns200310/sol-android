package io.github.ns200310.sol.dashboard.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Meter(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var simulatedTemp = 20.0f // base temperature in °C
    private var simulatedPressure = 1013.25f // base pressure in hPa
    private var simulatedHumidity = 50.0f // base humidity in %

    private val gravity = FloatArray(3)
    private var lastMovement = 0f

    fun start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Low-pass filter to isolate gravity
            val alpha = 0.8f
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

            // Calculate movement intensity
            val movement = sqrt(
                (event.values[0] - gravity[0]).pow(2) +
                        (event.values[1] - gravity[1]).pow(2) +
                        (event.values[2] - gravity[2]).pow(2)
            )

            // Simulate environmental changes based on movement
            if (abs(movement - lastMovement) > 0.5f) {
                simulatedTemp += (movement - 1.0f) * 0.05f
                simulatedPressure += (movement - 1.0f) * 0.1f
                simulatedHumidity -= (movement - 1.0f) * 0.2f

                // Clamp values to reasonable ranges
                simulatedTemp = simulatedTemp.coerceIn(15.0f, 35.0f)
                simulatedPressure = simulatedPressure.coerceIn(980.0f, 1040.0f)
                simulatedHumidity = simulatedHumidity.coerceIn(20.0f, 80.0f)

            }
            lastMovement = movement
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}