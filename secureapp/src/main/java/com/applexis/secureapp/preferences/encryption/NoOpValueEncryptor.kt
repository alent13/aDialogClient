package com.applexis.secureapp.preferences.encryption

import com.applexis.secureapp.encodeBase64String
import com.applexis.secureapp.serializers.*
import com.applexis.secureapp.decodeBase64

class NoOpValueEncryptor: ValueEncryptor {

    private val booleanSerializer = BooleanSerializer()
    private val intSerializer = IntSerializer()
    private val longSerializer = LongSerializer()
    private val floatSerializer = FloatSerializer()
    private val doubleSerializer = DoubleSerializer()
    private val stringSerializer = StringSerializer()
    private val stringSetSerializer = StringSetSerializer()

    override fun encryptBoolean(value: Boolean): String = booleanSerializer.serialize(value).encodeBase64String()
    override fun encryptInt(value: Int): String = intSerializer.serialize(value).encodeBase64String()
    override fun encryptLong(value: Long): String = longSerializer.serialize(value).encodeBase64String()
    override fun encryptFloat(value: Float): String = floatSerializer.serialize(value).encodeBase64String()
    override fun encryptDouble(value: Double): String = doubleSerializer.serialize(value).encodeBase64String()
    override fun encryptString(value: String): String = stringSerializer.serialize(value).encodeBase64String()
    override fun encryptStringSet(value: MutableSet<String>): String = stringSetSerializer.serialize(value).encodeBase64String()

    override fun decryptBoolean(value: String): Boolean = booleanSerializer.deserialize(value.decodeBase64())
    override fun decryptInt(value: String): Int = intSerializer.deserialize(value.decodeBase64())
    override fun decryptLong(value: String): Long = longSerializer.deserialize(value.decodeBase64())
    override fun decryptFloat(value: String): Float = floatSerializer.deserialize(value.decodeBase64())
    override fun decryptDouble(value: String): Double = doubleSerializer.deserialize(value.decodeBase64())
    override fun decryptString(value: String): String = stringSerializer.deserialize(value.decodeBase64())
    override fun decryptStringSet(value: String): Set<String> = stringSetSerializer.deserialize(value.decodeBase64())

}