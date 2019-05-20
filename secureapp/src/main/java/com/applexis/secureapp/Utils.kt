package com.applexis.secureapp

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

fun String.decodeBase64() = Base64.decode(this.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)

fun ByteArray.encodeBase64String() = String(Base64.encode(this, Base64.DEFAULT), StandardCharsets.UTF_8)
