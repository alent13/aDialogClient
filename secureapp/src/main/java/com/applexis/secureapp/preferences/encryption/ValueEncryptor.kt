package com.applexis.secureapp.preferences.encryption

/**
 * Created by applexis on 23.04.2019
 */
interface ValueEncryptor {

    @Throws(EncryptionException::class)
    fun encryptBoolean(value: Boolean): String

    @Throws(EncryptionException::class)
    fun encryptInt(value: Int): String

    @Throws(EncryptionException::class)
    fun encryptLong(value: Long): String

    @Throws(EncryptionException::class)
    fun encryptFloat(value: Float): String

    @Throws(EncryptionException::class)
    fun encryptDouble(value: Double): String

    @Throws(EncryptionException::class)
    fun encryptString(value: String): String

    @Throws(EncryptionException::class)
    fun encryptStringSet(value: MutableSet<String>): String

    @Throws(EncryptionException::class)
    fun decryptBoolean(value: String): Boolean

    @Throws(EncryptionException::class)
    fun decryptInt(value: String): Int

    @Throws(EncryptionException::class)
    fun decryptLong(value: String): Long

    @Throws(EncryptionException::class)
    fun decryptFloat(value: String): Float

    @Throws(EncryptionException::class)
    fun decryptDouble(value: String): Double

    @Throws(EncryptionException::class)
    fun decryptString(value: String): String

    @Throws(EncryptionException::class)
    fun decryptStringSet(value: String): Set<String>

}