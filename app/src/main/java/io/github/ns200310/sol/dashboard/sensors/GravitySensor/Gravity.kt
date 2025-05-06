package io.github.ns200310.sol.dashboard.sensors.GravitySensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class Gravity(
    context: Context,
    private val onGravityChanged: (FloatArray) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gravitySensor = sensorManager.getDefaultSensor(Sensor
        .TYPE_GRAVITY)

    fun startListening() {
        gravitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY) {
            onGravityChanged(event.values.clone())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
