package com.applexis.secureapp.serializers

/**
 * Created by applexis on 25.04.2019
 */
class FloatSerializer {

    fun serialize(value: Float): ByteArray = IntSerializer().serialize(value.toBits())

    fun deserialize(bytes: ByteArray): Float = Float.fromBits(IntSerializer().deserialize(bytes))

}