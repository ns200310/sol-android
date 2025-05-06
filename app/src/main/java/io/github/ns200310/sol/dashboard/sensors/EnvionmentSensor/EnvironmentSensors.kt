package io.github.ns200310.sol.dashboard.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class EnvironmentSensors(
    context: Context,
    private val onEnvironmentChanged: (temp: Float?, pressure: Float?, humidity: Float?) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    private val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private val humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

    private var currentTemp: Float? = null
    private var currentPressure: Float? = null
    private var currentHumidity: Float? = null

    fun startListening() {
        tempSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        pressureSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        humiditySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (event.sensor.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    currentTemp = event.values[0]
                }
                Sensor.TYPE_PRESSURE -> {
                    currentPressure = event.values[0]
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    currentHumidity = event.values[0]
                }
            }
            onEnvironmentChanged(currentTemp, currentPressure, currentHumidity)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}