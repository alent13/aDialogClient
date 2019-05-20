package com.applexis.secureapp.serializers

import java.nio.charset.StandardCharsets

class StringSerializer {

    fun serialize(value: String): ByteArray = value.toByteArray(StandardCharsets.UTF_8)

    fun deserialize(bytes: ByteArray): String = String(bytes, StandardCharsets.UTF_8)

}