package com.sijan.stepcounter

abstract class MeasurableSensor(
    protected open val sensorType: Int
) {
    abstract val doesSensorExist: Boolean

    abstract fun startListening()
    abstract fun stopListening()

    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }
}
