package com.applexis.secureapp

import android.content.Context
import android.provider.Settings


/**
 * Created by applexis on 18.05.2019
 */
class DeviceBasedSalt(context: Context) {

    val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val salt: ByteArray = SHA256Helper.getHash(deviceId)

}