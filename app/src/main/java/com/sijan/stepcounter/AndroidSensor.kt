package com.sijan.stepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

abstract class AndroidSensor(
    private val context: Context,
    override val sensorType: Int
) : MeasurableSensor(sensorType), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override val doesSensorExist: Boolean
        get() = sensor != null

    override fun startListening() {
        //Log.d("AndroidSensor", "Starting sensor")
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopListening() {
        Log.d("AndroidSensor", "Stopping sensor")
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val values = it.values.toList()
            //Log.d("AndroidSensor", "Sensor event: $values")
            onSensorValuesChanged?.invoke(values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ignore for now
    }
}
