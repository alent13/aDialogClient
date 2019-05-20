package com.applexis.secureapp.di

import com.applexis.secureapp.DeviceBasedSalt
import dagger.Component

/**
 * Created by applexis on 18.05.2019
 */
@Component(modules = [LibModule::class])
interface LibComponent {

    fun getDeviceBasedSalt(): DeviceBasedSalt

}