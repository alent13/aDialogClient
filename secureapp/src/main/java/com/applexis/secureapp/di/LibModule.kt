package com.applexis.secureapp.di

import android.content.Context
import com.applexis.secureapp.DeviceBasedSalt
import dagger.Module
import dagger.Provides

/**
 * Created by applexis on 18.05.2019
 */
@Module
class LibModule(private val context: Context) {

    @Provides
    fun getDeviceBasedSalt(): DeviceBasedSalt = DeviceBasedSalt(context)

}