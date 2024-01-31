package com.example.securebankingapp.data.localStorage

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EncryptedDataStorage @Inject constructor(@ApplicationContext context: Context) {
    private companion object {
        const val KEY_LOGIN_TRIES = "key_cookie_session"
        const val LOGIN_BITS_REQUESTS = "key_login_bits_requests"
    }

    private val spec = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .build()

    private val masterKey = MasterKey.Builder(context)
        .setKeyGenParameterSpec(spec)
        .build()

    private val pref = EncryptedSharedPreferences.create(
        context,
        "Encrypted_Shared_Preferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    internal fun getLoginBitsTries(): Int {
        val loginTries = pref.getInt(LOGIN_BITS_REQUESTS, 0)

        return loginTries
    }

    internal fun incrementLoginBitsTries() {
        val editor = pref.edit()
        val loginTries = pref.getInt(LOGIN_BITS_REQUESTS, 0)

        editor.putInt(KEY_LOGIN_TRIES, loginTries + 1)
        editor.apply()
    }

    internal fun resetLoginBitsTries() {
        val editor = pref.edit()

        editor.putInt(LOGIN_BITS_REQUESTS, 0)
        editor.apply()
    }


    internal fun getLoginTries(): Int {
        val loginTries = pref.getInt(KEY_LOGIN_TRIES, 0)

        return loginTries
    }

    internal fun incrementLoginTries() {
        val editor = pref.edit()
        val loginTries = pref.getInt(KEY_LOGIN_TRIES, 0)

        editor.putInt(KEY_LOGIN_TRIES, loginTries + 1)
        editor.apply()
    }

    internal fun resetLoginTries() {
        val editor = pref.edit()

        editor.putInt(KEY_LOGIN_TRIES, 0)
        editor.apply()
    }
}