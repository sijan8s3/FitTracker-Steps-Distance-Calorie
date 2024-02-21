package com.sijan.stepcounter

import android.content.Context
import android.hardware.Sensor

/*class StepSensor(
    context: Context
): AndroidSensor(
    context = context,
    //sensorFeature = PackageManager.FEATURE_SENSOR_STEP_COUNTER,
    sensorType = Sensor.TYPE_STEP_COUNTER
)*/


class AccSensor(
    context: Context
): AndroidSensor(
    context = context,
    //sensorFeature = PackageManager.FEATURE_SENSOR_PROXIMITY,
    sensorType = Sensor.TYPE_ACCELEROMETER
)
