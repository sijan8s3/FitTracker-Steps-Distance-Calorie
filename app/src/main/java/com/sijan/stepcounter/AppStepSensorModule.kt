package com.sijan.stepcounter

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppStepSensorModule {

    /*@Provides
    @Singleton
    fun provideStepSensor(app:Application): MeasurableSensor {
        return StepSensor(app)
    }*/
    @Provides
    @Singleton
    fun provideAccSensor(app:Application): MeasurableSensor {
        return AccSensor(app)
    }
}