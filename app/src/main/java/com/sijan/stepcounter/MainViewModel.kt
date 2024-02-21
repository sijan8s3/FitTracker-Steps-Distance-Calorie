package com.sijan.stepcounter

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sqrt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accelerometerSensor: MeasurableSensor
) : ViewModel() {

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    private val _distanceTraveled = MutableStateFlow(0.0)
    val distanceTraveled: StateFlow<Double> = _distanceTraveled

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val stepDetector = StepDetector()

//    init {
//        startTracking()
//    }

    fun startTracking() {
        accelerometerSensor.startListening()
        _isTracking.value=true
        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            val magnitude = calculateMagnitude(values)
            //val threshold = calculateThreshold() // You need to implement this method
            val threshold = 17.00
            //println("threshold: "+threshold)
            if (stepDetector.detect(magnitude, threshold)) {
                _stepCount.value = stepDetector.getStepCount()
                updateDistance()
                updateCaloriesBurned()
            }
        }
    }

    fun stopTracking() {
        accelerometerSensor.stopListening()
        _isTracking.value=false
    }

    private fun calculateMagnitude(values: List<Float>): Double {
        // Calculate magnitude vector from accelerometer data
        val sumOfSquares = values.map { it * it }.sum()
        return sqrt(sumOfSquares.toDouble())
    }

    private fun updateDistance() {
        // Implement distance calculation based on step count and stride length
        val strideLength = calculateStrideLength() // You need to implement this method
        _distanceTraveled.value = _stepCount.value * strideLength
    }

    private fun updateCaloriesBurned() {
        // Implement calorie calculation based on distance traveled and user's weight
        val weight = 70 // Example weight in kilograms
        val caloriesPerMeter = 0.04 // Example calories burned per meter walked
        _caloriesBurned.value = _distanceTraveled.value * caloriesPerMeter * weight
    }

    private fun calculateStrideLength(): Double {
        // Implement stride length calculation based on user's height
        // Example: Average stride length is approximately 0.415 * height (in centimeters)
        val heightInCentimeters = 170 // Example height in centimeters
        return 0.415 * heightInCentimeters / 100 // Convert height to meters
    }


    fun calculateThreshold(): Double {
        val maxTempoInMillis = 60000 / MAX_TEMPO_BPM.toDouble() // Maximum tempo in milliseconds per step
        val maxSamplesToOmit = (maxTempoInMillis / SENSOR_FREQUENCY_HZ).roundToInt() // Maximum number of samples to omit

        // Threshold calculation based on the number of samples to omit
        return maxSamplesToOmit.toDouble()
    }

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }

    fun calculateThreshold(accelerometerValues: List<Float>): Double {
        // Calculate magnitude vector from accelerometer data
        val sumOfSquares = accelerometerValues.map { it * it }.sum()
        val accelerationMagnitude = Math.sqrt(sumOfSquares.toDouble())

        // Determine the current device orientation or angle
        val deviceAngle = calculateDeviceAngle(accelerometerValues)

        // Adjust the threshold based on the device orientation or angle
        val baseThreshold = if (deviceAngle < 45 || deviceAngle > 135) {
            // Horizontal orientation (e.g., walking)
            // Use a lower threshold
            10.0
        } else {
            // Vertical orientation (e.g., slight raise or tilt)
            // Use a higher threshold
            20.0
        }

        // Adjust the threshold based on the acceleration magnitude
        val adjustedThreshold = baseThreshold * (accelerationMagnitude / SensorManager.GRAVITY_EARTH)

        return adjustedThreshold
    }

    private fun calculateDeviceAngle(accelerometerValues: List<Float>): Double {
        // Calculate the angle of the device based on accelerometer data
        // For simplicity, you can use the pitch or roll angles
        val x = accelerometerValues[0]
        val y = accelerometerValues[1]
        val z = accelerometerValues[2]

        val angleRad = Math.atan2(Math.sqrt(((x * x) + (z * z)).toDouble()), y.toDouble())
        val angleDeg = Math.toDegrees(angleRad)

        return angleDeg
    }
}

private const val MAX_TEMPO_BPM = 120
private const val SENSOR_FREQUENCY_HZ = 50 // Sensor frequency in Hz
private const val MAX_SAMPLE_RATE = 20 // Maximum sample rate in milliseconds

