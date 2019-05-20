package com.applexis.secureapp.preferences.encryption

/**
 * @author applexis
 */
interface KeyEncryptor {

    @Throws(EncryptionException::class)
    fun encrypt(key: String): String

    @Throws(EncryptionException::class)
    fun decrypt(key: String): String

}