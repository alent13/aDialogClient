package com.applexis.secureapp.accounts.encryption

import android.os.Bundle

/**
 * Created by applexis on 15.05.2019
 */
interface AccountEncryptor {

    fun encrypt(value: String): String
    fun encryptBundle(bundle: Bundle): Bundle

    fun decrypt(value: String): String
    fun decryptBundle(bundle: Bundle): Bundle

}