package com.applexis.secureapp.preferences.encryption

class NoOpKeyEncryptor: KeyEncryptor {
    override fun decrypt(key: String) = key
    override fun encrypt(key: String) = key
}