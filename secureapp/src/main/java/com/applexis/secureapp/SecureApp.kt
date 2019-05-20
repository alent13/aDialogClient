package com.applexis.secureapp

import android.content.Context
import com.applexis.secureapp.di.DaggerLibComponent
import com.applexis.secureapp.di.LibComponent
import com.applexis.secureapp.di.LibModule

/**
 * Created by applexis on 18.05.2019
 */
class SecureApp private constructor(context: Context) {

    companion object {
        lateinit var instance: SecureApp

        fun initialize(context: Context) {
            instance = SecureApp(context)
        }
    }

    var component: LibComponent = DaggerLibComponent.builder()
        .libModule(LibModule(context))
        .build()

}