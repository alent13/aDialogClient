package com.applexis.secureapp.accounts.encryption

import android.os.Bundle
import com.applexis.secureapp.*
import com.applexis.secureapp.preferences.encryption.EncryptionException

/**
 * Created by applexis on 15.05.2019
 */
class AesAccountEncryptor(val password: String): AccountEncryptor {

    val aesCryptoHelper: AesCryptoHelper

    init {
        val secretKeyBytes = PBKDF2Helper.generateKey(password, SecureApp.instance.component.getDeviceBasedSalt().salt)
        aesCryptoHelper = AesCryptoHelper(secretKeyBytes)
    }

    override fun encryptBundle(bundle: Bundle): Bundle {
        bundle.keySet().forEach {
            val value = bundle.getString(it)
            if (value != null) {
                bundle.putString(it, encrypt(value))
            }
        }
        return bundle
    }

    override fun decryptBundle(bundle: Bundle): Bundle {
        bundle.keySet().forEach {
            val value = bundle.getString(it)
            if (value != null) {
                bundle.putString(it, encrypt(value))
            }
        }
        return bundle
    }

    override fun encrypt(value: String): String {
        try {
            return aesCryptoHelper.encrypt(value.decodeBase64()).encodeBase64String()
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }

    override fun decrypt(value: String): String {
        try {
            return aesCryptoHelper.decrypt(value.decodeBase64()).encodeBase64String()
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }

}