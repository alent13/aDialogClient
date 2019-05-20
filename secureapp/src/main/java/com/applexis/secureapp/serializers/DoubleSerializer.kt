package com.applexis.secureapp.serializers

class DoubleSerializer {

    fun serialize(value: Double): ByteArray = LongSerializer().serialize(value.toBits())

    fun deserialize(bytes: ByteArray): Double = Double.fromBits(LongSerializer().deserialize(bytes))
}