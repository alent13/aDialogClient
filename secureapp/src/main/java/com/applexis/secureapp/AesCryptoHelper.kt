package com.applexis.secureapp

import com.applexis.secureapp.preferences.encryption.EncryptionException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by applexis on 16.05.2019
 */
class AesCryptoHelper {

    companion object {
        private val AES = "AES"
        private val AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5PADDING"
    }

    private val initialVector: ByteArray
    private val secretKeyBytes: ByteArray

    constructor(secretKeyBytes: ByteArray, initialVector: ByteArray) {
        this.secretKeyBytes = secretKeyBytes
        this.initialVector = initialVector
    }

    constructor(secret: SecretKey) {
        this.secretKeyBytes = secret.encoded
        val size = secret.encoded.size
        this.initialVector = (0 until size step size / 16).map { secret.encoded[it] }.toByteArray()
    }

    fun decrypt(key: ByteArray): ByteArray {
        try {
            val decryptCipher = createDecryptCipher()
            return decryptCipher.doFinal(key)
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }

    fun encrypt(key: ByteArray): ByteArray {
        try {
            val encryptCipher = createEncryptCipher()
            return encryptCipher.doFinal(key)
        } catch (e: Exception) {
            throw EncryptionException(e)
        }
    }

    @Throws(Exception::class)
    private fun createEncryptCipher(): Cipher {
        val secretKeySpec = SecretKeySpec(secretKeyBytes, AES)
        val iv = IvParameterSpec(initialVector)
        val cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)
        return cipher
    }

    @Throws(Exception::class)
    private fun createDecryptCipher(): Cipher {
        val secretKeySpec = SecretKeySpec(secretKeyBytes, AES)
        val iv = IvParameterSpec(initialVector)
        val cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)
        return cipher
    }

}