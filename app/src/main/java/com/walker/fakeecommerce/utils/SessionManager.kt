package com.walker.fakeecommerce.utils

import android.content.SharedPreferences
import com.walker.fakeecommerce.model.AccessToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
    private val LOGIN_DATE_KEY = "LOGIN_DATE_KEY"

    fun writeToken(accessToken: AccessToken): Boolean {
        return try {
            val sharedPreferencesEdit = sharedPreferences.edit()

            sharedPreferencesEdit.putString(ACCESS_TOKEN_KEY, accessToken.accessToken)

            val simpleFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = simpleFormatDate.format(Date())

            sharedPreferencesEdit.putString(LOGIN_DATE_KEY, date)
            sharedPreferencesEdit.apply()

            true
        } catch (e: Exception) {
            false
        }
    }

    fun readToken() = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    fun readDate() = sharedPreferences.getString(LOGIN_DATE_KEY, null)

    fun logout() = sharedPreferences.edit().clear().apply()

}