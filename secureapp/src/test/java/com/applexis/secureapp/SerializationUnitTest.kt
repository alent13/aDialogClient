package com.applexis.secureapp

import com.applexis.secureapp.serializers.*
import org.junit.Test

import org.junit.Assert.*

class SerializationUnitTest {

    @Test
    fun boolean() {
        val booleanSerializer = BooleanSerializer()
        assertEquals(true, booleanSerializer.deserialize(booleanSerializer.serialize(true)))
    }

    @Test
    fun int() {
        val intSerializer = IntSerializer()
        assertEquals(0, intSerializer.deserialize(intSerializer.serialize(0)))
        assertEquals(-1, intSerializer.deserialize(intSerializer.serialize(-1)))
        assertEquals(42, intSerializer.deserialize(intSerializer.serialize(42)))
        assertEquals(Int.MAX_VALUE, intSerializer.deserialize(intSerializer.serialize(Int.MAX_VALUE)))
        assertEquals(Int.MIN_VALUE, intSerializer.deserialize(intSerializer.serialize(Int.MIN_VALUE)))
    }

    @Test
    fun float() {
        val floatSerializer = FloatSerializer()
        assertEquals(0f, floatSerializer.deserialize(floatSerializer.serialize(0f)))
        assertEquals(0.000001f, floatSerializer.deserialize(floatSerializer.serialize(0.000001f)))
        assertEquals(-1f, floatSerializer.deserialize(floatSerializer.serialize(-1f)))
        assertEquals(Float.MAX_VALUE, floatSerializer.deserialize(floatSerializer.serialize(Float.MAX_VALUE)))
        assertEquals(Float.MIN_VALUE, floatSerializer.deserialize(floatSerializer.serialize(Float.MIN_VALUE)))
    }

    @Test
    fun long() {
        val longSerializer = LongSerializer()
        assertEquals(0L, longSerializer.deserialize(longSerializer.serialize(0L)))
        assertEquals(42L, longSerializer.deserialize(longSerializer.serialize(42L)))
        assertEquals(-1L, longSerializer.deserialize(longSerializer.serialize(-1L)))
        assertEquals(Long.MAX_VALUE, longSerializer.deserialize(longSerializer.serialize(Long.MAX_VALUE)))
        assertEquals(Long.MIN_VALUE, longSerializer.deserialize(longSerializer.serialize(Long.MIN_VALUE)))
    }

    @Test
    fun double() {
        val doubleSerializer = DoubleSerializer()
        assertEquals(0.0, doubleSerializer.deserialize(doubleSerializer.serialize(0.0)), 0.000000000001)
        assertEquals(42.0, doubleSerializer.deserialize(doubleSerializer.serialize(42.0)), 0.000000000001)
        assertEquals(-1.0, doubleSerializer.deserialize(doubleSerializer.serialize(-1.0)), 0.000000000001)
        assertEquals(Double.MAX_VALUE, doubleSerializer.deserialize(doubleSerializer.serialize(Double.MAX_VALUE)), 0.000000000001)
        assertEquals(Double.MIN_VALUE, doubleSerializer.deserialize(doubleSerializer.serialize(Double.MIN_VALUE)), 0.000000000001)
        assertEquals(Double.NEGATIVE_INFINITY, doubleSerializer.deserialize(doubleSerializer.serialize(Double.NEGATIVE_INFINITY)), 0.000000000001)
        assertEquals(Double.POSITIVE_INFINITY, doubleSerializer.deserialize(doubleSerializer.serialize(Double.POSITIVE_INFINITY)), 0.000000000001)
    }

    @Test
    fun string() {
        val stringSerializer = StringSerializer()
        assertEquals("", stringSerializer.deserialize(stringSerializer.serialize("")))
        assertEquals("test", stringSerializer.deserialize(stringSerializer.serialize("test")))
    }

    @Test
    fun stringSet() {
        val stringSetSerializer = StringSetSerializer()
        assertEquals(setOf<String>(), stringSetSerializer.deserialize(stringSetSerializer.serialize(setOf())))
        assertEquals(setOf("test"), stringSetSerializer.deserialize(stringSetSerializer.serialize(setOf("test"))))
        assertEquals(setOf("test", "test2"), stringSetSerializer.deserialize(stringSetSerializer.serialize(setOf("test", "test2"))))
    }

}