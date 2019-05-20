package com.applexis.secureapp.serializers

class BooleanSerializer {

    fun serialize(value: Boolean): ByteArray = byteArrayOf(if (value) 1 else 0)

    fun deserialize(bytes: ByteArray): Boolean = bytes[0] == 1.toByte()

}