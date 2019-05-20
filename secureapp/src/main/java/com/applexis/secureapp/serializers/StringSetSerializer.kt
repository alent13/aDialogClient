package com.applexis.secureapp.serializers

import java.util.Collections
import kotlin.collections.HashSet


/**
 * {@code Set<String>} to byte array implementation and backwards
 * {@Link} https://github.com/yandextaxitech/binaryprefs/blob/master/library/src/main/java/com/ironz/binaryprefs/serialization/serializer/stringSetSerializer.java
 */
class StringSetSerializer {

    private val intSerializer = IntSerializer()
    private val stringSerializer = StringSerializer()

    fun serialize(set: Set<String>): ByteArray {
        val bytes = arrayOfNulls<ByteArray>(set.size)
        var totalArraySize = 0

        for ((i, s) in set.withIndex()) {
            val stringBytes = stringSerializer.serialize(s)
            val stringSizeBytes = intSerializer.serialize(stringBytes.size)

            val merged = ByteArray(stringBytes.size + stringSizeBytes.size)

            System.arraycopy(stringSizeBytes, 0, merged, 0, stringSizeBytes.size)
            System.arraycopy(stringBytes, 0, merged, stringSizeBytes.size, stringBytes.size)

            bytes[i] = merged

            totalArraySize += merged.size
        }

        val totalArray = ByteArray(totalArraySize)

        var offset = 0
        for (b in bytes) {
            System.arraycopy(b, 0, totalArray, offset, b!!.size)
            offset += b.size
        }

        return totalArray
    }

    fun deserialize(bytes: ByteArray): Set<String> {
        val set = HashSet<String>()

        var i = 0

        while (i < bytes.size) {
            val integerBytesSize = Integer.SIZE / 8
            val stringSizeBytes = ByteArray(integerBytesSize)
            System.arraycopy(bytes, i, stringSizeBytes, 0, stringSizeBytes.size)
            val stringSize = intSerializer.deserialize(stringSizeBytes)

            val stringBytes = ByteArray(stringSize)

            for (k in stringBytes.indices) {
                val stringOffset = i + k + integerBytesSize
                stringBytes[k] = bytes[stringOffset]
            }

            set.add(stringSerializer.deserialize(stringBytes))

            i += integerBytesSize + stringSize
        }

        return Collections.unmodifiableSet(set)
    }

}