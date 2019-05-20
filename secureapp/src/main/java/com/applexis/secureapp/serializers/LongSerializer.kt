package com.applexis.secureapp.serializers

class LongSerializer {

    var longMask = 0xffL

    fun serialize(value: Long): ByteArray = byteArrayOf(
        value.shr(56).toByte(),
        value.shr(48).toByte(),
        value.shr(40).toByte(),
        value.shr(32).toByte(),
        value.shr(24).toByte(),
        value.shr(16).toByte(),
        value.shr(8).toByte(),
        value.toByte()
    )

    fun deserialize(bytes: ByteArray): Long =
        bytes[7].toLong().and(longMask) +
        bytes[6].toLong().and(longMask).shl(8) +
        bytes[5].toLong().and(longMask).shl(16) +
        bytes[4].toLong().and(longMask).shl(24) +
        bytes[3].toLong().and(longMask).shl(32) +
        bytes[2].toLong().and(longMask).shl(40) +
        bytes[1].toLong().and(longMask).shl(48) +
        bytes[0].toLong().shl(56)

}