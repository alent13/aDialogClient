package com.applexis.secureapp.preferences.encryption

import java.lang.RuntimeException

class EncryptionException : RuntimeException {
    constructor(message: String): super(message)
    constructor(t: Throwable): super(t)
}
