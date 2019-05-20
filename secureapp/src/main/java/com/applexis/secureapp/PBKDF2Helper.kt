package com.applexis.secureapp

import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Created by applexis on 17.05.2019
 */
object PBKDF2Helper {

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateKey(pass: String, salt: ByteArray, iterations: Int = 1000, outputKeyLength: Int = 256): SecretKey {
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val keySpec = PBEKeySpec(pass.toCharArray(), salt, iterations, outputKeyLength)
        return secretKeyFactory.generateSecret(keySpec)
    }

}