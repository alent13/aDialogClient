package com.applexis.secureapp.accounts

import android.os.Bundle

interface IAccountManagerResponse {
    fun onResult(value: Bundle?)
    fun onError(errorCode: Int, errorMessage: String)
}
