package com.applexis.secureapp.preferences.encryption

import com.applexis.secureapp.*
import java.nio.charset.StandardCharsets

/**
 * Created by applexis on 23.04.2019
 */
class AesKeyEncryptor(val password: String): KeyEncryptor {

    companion object {
        private val AES = "AES"
        private val AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5PADDING"
        private val KEY_LENGTH = 16

        private val SHORT_KEYS_MESSAGE = "Secret and initial vector must be 16 bytes"
    }

    val aesCryptoHelper: AesCryptoHelper

    init {
        val secretKeyBytes = PBKDF2Helper.generateKey(password, SecureApp.instance.component.getDeviceBasedSalt().salt)
        aesCryptoHelper = AesCryptoHelper(secretKeyBytes)
    }

    override fun decrypt(key: String): String {
        try {
            return String(aesCryptoHelper.decrypt(key.decodeBase64()), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }

    override fun encrypt(key: String): String {
        try {
            return aesCryptoHelper.encrypt(key.toByteArray(StandardCharsets.UTF_8)).encodeBase64String()
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }
}