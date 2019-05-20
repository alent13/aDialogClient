package com.applexis.adialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.applexis.secureapp.DeviceBasedSalt
import com.applexis.secureapp.SecureApp
import com.applexis.secureapp.accounts.SecureAccountManager
import com.applexis.secureapp.preferences.encryption.AesKeyEncryptor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SecureAccountManager(this, )

        findViewById<TextView>(R.id.label).text = "$text\n$eText\n$dText\n\n$salt"
    }
}
