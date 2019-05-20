package com.applexis.secureapp

import org.junit.Test

/**
 * Created by applexis on 18.05.2019
 */
class EncryptionUnitTest {

    @Test
    fun bytes() {
        val b = (1..256 step 16).toList()
        b.forEach { println(it) }
    }

}