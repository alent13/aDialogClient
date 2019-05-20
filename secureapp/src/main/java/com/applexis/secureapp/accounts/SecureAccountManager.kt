package com.applexis.secureapp.accounts

import android.accounts.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.util.Log
import com.applexis.secureapp.accounts.encryption.AccountEncryptor
import java.io.IOException
import java.util.concurrent.*
import android.os.Build
import android.os.Looper
import android.accounts.AuthenticatorException
import android.os.DeadSystemException
import android.os.DeadObjectException
import android.accounts.AccountManager
import android.R.attr.name

/**
 * Created by applexis on 15.05.2019
 */
@SuppressLint("MissingPermission")
class SecureAccountManager(
    private val mContext: Context,
    private val mAccountManager: AccountManager,
    private val mAccountEncryptor: AccountEncryptor
) {
    private val TAG = "SecureAccountManager"

    val ERROR_CODE_REMOTE_EXCEPTION = 1
    val ERROR_CODE_NETWORK_ERROR = 3
    val ERROR_CODE_CANCELED = 4
    val ERROR_CODE_INVALID_RESPONSE = 5
    val ERROR_CODE_UNSUPPORTED_OPERATION = 6
    val ERROR_CODE_BAD_ARGUMENTS = 7
    val ERROR_CODE_BAD_REQUEST = 8
    val ERROR_CODE_BAD_AUTHENTICATION = 9

    val ERROR_CODE_USER_RESTRICTED = 100
    val ERROR_CODE_MANAGEMENT_DISABLED_FOR_ACCOUNT_TYPE = 101

    private val mMainHandler = Handler(mContext.mainLooper)

    fun getPassword(account: Account): String =
        mAccountEncryptor.decrypt(mAccountManager.getPassword(account))

    fun getUserData(account: Account, key: String): String =
        mAccountEncryptor.decrypt(mAccountManager.getUserData(account, key))

    fun addAccountExplicitly(account: Account, password: String, userdata: Bundle): Boolean =
        mAccountManager.addAccountExplicitly(
            account,
            mAccountEncryptor.encrypt(password),
            mAccountEncryptor.encryptBundle(userdata)
        )

    fun peekAuthToken(account: Account, authTokenType: String): String =
        mAccountEncryptor.decrypt(mAccountManager.peekAuthToken(account, authTokenType))

    fun setUserData(account: Account, key: String, value: String) =
        mAccountManager.setUserData(account, mAccountEncryptor.encrypt(key), mAccountEncryptor.encrypt(value))

    fun setAuthToken(account: Account, authTokenType: String, authToken: String) =
        mAccountManager.setAuthToken(account, authTokenType, mAccountEncryptor.encrypt(authToken))

    fun blockingGetAuthToken(account: Account, authTokenType: String, notifyAuthFailure: Boolean): String =
        mAccountManager.blockingGetAuthToken(account, authTokenType, notifyAuthFailure)

    fun getAuthToken(
        account: Account, authTokenType: String, options: Bundle, activity: Activity,
        callback: AccountManagerCallback<Bundle>, handler: Handler
    ): AccountManagerFuture<Bundle>? =
        object : AmsTask(activity, handler, callback) {
            override fun doWork() {
                val authToken = mAccountManager.blockingGetAuthToken(account, authTokenType, false)

                val result = Bundle()
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                mResponse.onResult(result)
            }
        }.start()

    private fun ensureNotOnMainThread() {
        val looper = Looper.myLooper()
        if (looper != null && looper == mContext.mainLooper) {
            val exception = IllegalStateException(
                "calling this from your main thread can lead to deadlock"
            )
            Log.e(
                TAG, "calling this from your main thread can lead to deadlock and/or ANRs",
                exception
            )
            if (mContext.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.FROYO) {
                throw exception
            }
        }
    }

    private fun postToHandler(
        handler: Handler?,
        callback: AccountManagerCallback<Bundle>,
        future: AccountManagerFuture<Bundle>
    ) {
        val handler = handler ?: mMainHandler
        handler.post {
            callback.run(future)
        }
    }

    private fun convertErrorToException(code: Int, message: String): Exception {
        if (code == ERROR_CODE_NETWORK_ERROR) {
            return IOException(message)
        }
        if (code == ERROR_CODE_UNSUPPORTED_OPERATION) {
            return UnsupportedOperationException(message)
        }
        if (code == ERROR_CODE_INVALID_RESPONSE) {
            return AuthenticatorException(message)
        }
        return if (code == ERROR_CODE_BAD_ARGUMENTS) {
            IllegalArgumentException(message)
        } else AuthenticatorException(message)
    }

    private abstract inner class AmsTask(
        internal val mActivity: Activity?,
        internal val mHandler: Handler,
        internal val mCallback: AccountManagerCallback<Bundle>?
    ) : FutureTask<Bundle>(Callable<Bundle> { throw IllegalStateException("this should never be called") }),
        AccountManagerFuture<Bundle> {

        internal val mResponse: IAccountManagerResponse

        init {
            mResponse = Response()
        }

        fun start(): AccountManagerFuture<Bundle> {
            try {
                doWork()
            } catch (e: RemoteException) {
                setException(e)
            }

            return this
        }

        override fun set(bundle: Bundle?) {
            if (bundle == null) {
                Log.e(TAG, "the bundle must not be null", Exception())
            }
            super.set(bundle)
        }

        @Throws(RemoteException::class)
        abstract fun doWork()

        @Throws(OperationCanceledException::class, IOException::class, AuthenticatorException::class)
        private fun internalGetResult(timeout: Long?, unit: TimeUnit?): Bundle {
            if (!isDone) {
                ensureNotOnMainThread()
            }
            try {
                return timeout?.let { get(it, unit) } ?: get()
            } catch (e: CancellationException) {
                throw OperationCanceledException()
            } catch (e: TimeoutException) {
                // fall through and cancel
            } catch (e: InterruptedException) {
                // fall through and cancel
            } catch (e: ExecutionException) {
                when (val cause = e.cause) {
                    is IOException -> throw cause
                    is UnsupportedOperationException -> throw AuthenticatorException(cause)
                    is AuthenticatorException -> throw cause
                    is RuntimeException -> throw cause
                    is Error -> throw cause
                    else -> throw IllegalStateException(cause)
                }
            } finally {
                cancel(true /* interrupt if running */)
            }
            throw OperationCanceledException()
        }

        @Throws(OperationCanceledException::class, IOException::class, AuthenticatorException::class)
        override fun getResult(): Bundle {
            return internalGetResult(null, null)
        }

        @Throws(OperationCanceledException::class, IOException::class, AuthenticatorException::class)
        override fun getResult(timeout: Long, unit: TimeUnit): Bundle {
            return internalGetResult(timeout, unit)
        }

        override fun done() {
            if (mCallback != null) {
                postToHandler(mHandler, mCallback, this)
            }
        }

        /** Handles the responses from the AccountManager  */
        private inner class Response : IAccountManagerResponse {
            override fun onResult(value: Bundle?) {
                if (value == null) {
                    onError(AccountManager.ERROR_CODE_INVALID_RESPONSE, "null bundle returned")
                    return
                }
                val intent = value.getParcelable<Intent>(AccountManager.KEY_INTENT)
                if (intent != null && mActivity != null) {
                    // since the user provided an Activity we will silently start intents
                    // that we see
                    mActivity.startActivity(intent)
                    // leave the Future running to wait for the real response to this request
                } else if (value.getBoolean("retry")) {
                    try {
                        doWork()
                    } catch (e: RemoteException) {
                        if (e is DeadObjectException) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                throw RuntimeException(DeadSystemException())
                            } else {
                                throw RuntimeException(e)
                            }
                        } else {
                            throw RuntimeException(e)
                        }
                    }

                } else {
                    set(value)
                }
            }

            override fun onError(errorCode: Int, errorMessage: String) {
                if (errorCode == AccountManager.ERROR_CODE_CANCELED || errorCode == ERROR_CODE_USER_RESTRICTED
                    || errorCode == ERROR_CODE_MANAGEMENT_DISABLED_FOR_ACCOUNT_TYPE
                ) {
                    // the authenticator indicated that this request was canceled or we were
                    // forbidden to fulfill; cancel now
                    cancel(true /* mayInterruptIfRunning */)
                    return
                }
                setException(convertErrorToException(errorCode, errorMessage))
            }
        }
    }

}