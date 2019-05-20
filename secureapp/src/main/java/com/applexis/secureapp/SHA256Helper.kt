package com.applexis.secureapp

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by applexis on 18.05.2019
 */
object SHA256Helper {

    @Throws(NoSuchAlgorithmException::class)
    fun getHash(text: String): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(text.toByteArray())
        return md.digest()
    }

    @Throws(NoSuchAlgorithmException::class)
    fun getHashString(text: String): String = getHash(text).encodeBase64String()

}