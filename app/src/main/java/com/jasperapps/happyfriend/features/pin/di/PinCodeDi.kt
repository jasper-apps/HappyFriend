package com.jasperapps.happyfriend.features.pin.di

import android.content.SharedPreferences
import com.jasperapps.happyfriend.features.pin.data.PinCodeController
import com.jasperapps.happyfriend.features.pin.data.SharedPrefPinCodeController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PinCodeDi {
    @Provides
    fun providePinCodeController(sharedPreferences: SharedPreferences): PinCodeController {
        return SharedPrefPinCodeController(sharedPreferences)
    }
}
