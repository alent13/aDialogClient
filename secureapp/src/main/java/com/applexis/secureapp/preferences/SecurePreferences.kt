package com.applexis.secureapp.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.applexis.secureapp.preferences.encryption.*

class SecurePreferences private constructor(
    sharedPreferences: SharedPreferences,
    keyEncryptor: KeyEncryptor,
    valueEncryptor: ValueEncryptor
) : SharedPreferences {

    companion object {
        private const val _NAME = "default"
        private const val DEFAULT_NAME = "default"
    }

    private var mSharedPreferences: SharedPreferences = sharedPreferences
    private var mKeyEncryptor: KeyEncryptor = keyEncryptor
    private var mValueEncryptor: ValueEncryptor = valueEncryptor

    override fun contains(key: String): Boolean = mSharedPreferences.contains(mKeyEncryptor.encrypt(key))

    override fun getAll(): MutableMap<String, *> = mSharedPreferences.all

    private fun <T> getValue(key: String, defValue: T): T {
        if (!mSharedPreferences.contains(mKeyEncryptor.encrypt(key))) {
            return defValue
        }
        try {
            val value = mSharedPreferences.getString(mKeyEncryptor.encrypt(key), "")
            return when (defValue) {
                is Boolean -> mValueEncryptor.decryptBoolean(value) as T
                is Int -> mValueEncryptor.decryptInt(value) as T
                is Long -> mValueEncryptor.decryptLong(value) as T
                is Float -> mValueEncryptor.decryptFloat(value) as T
                is Double -> mValueEncryptor.decryptDouble(value) as T
                is String -> mValueEncryptor.decryptString(value) as T
                else -> defValue
            }
        } catch (e: EncryptionException) {
            Log.e("SECUREAPP", e.message)
            return defValue
        }
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getValue(key, defValue)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return getValue(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return getValue(key, defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return getValue(key, defValue)
    }

    fun getDouble(key: String, defValue: Double): Double {
        return getValue(key, defValue)
    }

    override fun getString(key: String, defValue: String): String {
        return getValue(key, defValue)
    }

    override fun getStringSet(key: String, defValue: MutableSet<String>): MutableSet<String> {
        if (!mSharedPreferences.contains(mKeyEncryptor.encrypt(key))) {
            return defValue
        }
        try {
            val value = mSharedPreferences.getString(mKeyEncryptor.encrypt(key), "")
            return mValueEncryptor.decryptStringSet(value).toMutableSet()
        } catch (e: EncryptionException) {
            Log.e("SECUREAPP", e.message)
            return defValue
        }
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) =
        mSharedPreferences.registerOnSharedPreferenceChangeListener(p0)

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) =
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(p0)

    override fun edit(): SharedPreferences.Editor = Editor(mSharedPreferences.edit(), mKeyEncryptor, mValueEncryptor)

    class Editor(
        private val editor: SharedPreferences.Editor,
        private val mKeyEncryptor: KeyEncryptor,
        private val mValueEncryptor: ValueEncryptor
    ) : SharedPreferences.Editor {

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptBoolean(value))

        override fun putInt(key: String, value: Int): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptInt(value))

        override fun putLong(key: String, value: Long): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptLong(value))

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptFloat(value))

        fun putDouble(key: String, value: Double): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptDouble(value))

        override fun putString(key: String, value: String): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptString(value))

        override fun putStringSet(key: String, value: MutableSet<String>): SharedPreferences.Editor =
            editor.putString(mKeyEncryptor.encrypt(key), mValueEncryptor.encryptStringSet(value))

        override fun remove(key: String?): SharedPreferences.Editor = apply { editor.remove(key) }

        override fun clear(): SharedPreferences.Editor = apply { editor.clear() }

        override fun commit(): Boolean = editor.commit()

        override fun apply() = editor.apply()

    }

    class Builder(val context: Context) {

        private var name: String = DEFAULT_NAME
        private var mode: Int = Context.MODE_PRIVATE
        private var keyEncryptor: KeyEncryptor? = null
        private var valueEncryptor: ValueEncryptor? = null

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun mode(mode: Int): Builder {
            this.mode = mode
            return this
        }

        fun keyEncryptor(keyEncryptor: KeyEncryptor): Builder {
            this.keyEncryptor = keyEncryptor
            return this
        }

        fun valueEncryptor(valueEncryptor: ValueEncryptor): Builder {
            this.valueEncryptor = valueEncryptor
            return this
        }

        fun build() = SecurePreferences(
            context.getSharedPreferences(name, mode),
            keyEncryptor ?: NoOpKeyEncryptor(),
            valueEncryptor ?: NoOpValueEncryptor()
        )

    }

}