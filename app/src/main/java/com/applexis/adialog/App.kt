package com.applexis.adialog

import android.app.Application
import com.applexis.secureapp.SecureApp

/**
 * Created by applexis on 18.05.2019
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SecureApp.initialize(applicationContext)
    }

}