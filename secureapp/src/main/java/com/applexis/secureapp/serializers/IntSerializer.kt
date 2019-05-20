package com.applexis.secureapp.serializers

import kotlin.experimental.and

class IntSerializer {

    private var mask = 0xff.toByte()
    private var intMask = 0xff

    fun serialize(value: Int): ByteArray = byteArrayOf(
        value.shr(24).toByte().and(mask),
        value.shr(16).toByte().and(mask),
        value.shr(8).toByte().and(mask),
        value.toByte().and(mask)
    )

    fun deserialize(bytes: ByteArray): Int =
            bytes[0].toInt().and(intMask).shl(24) +
            bytes[1].toInt().and(intMask).shl(16) +
            bytes[2].toInt().and(intMask).shl(8) +
            bytes[3].toInt().and(intMask)

}