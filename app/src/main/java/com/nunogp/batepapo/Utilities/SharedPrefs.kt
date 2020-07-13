package com.nunogp.batepapo.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context) {
    val PREFS_FILENAME = "prefs"
    // 0 = private
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    //const for the keys
    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USE_EMAIL = "userEmail"

    //costume geters and setters
    var isLoggedIn: Boolean
        get()= prefs.getBoolean(IS_LOGGED_IN, false) //se for false faz set
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String?
        get()= prefs.getString(AUTH_TOKEN, "")
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String?
        get()= prefs.getString(USE_EMAIL, "")
        set(value) = prefs.edit().putString(USE_EMAIL, value).apply()

    //serve sempre o mesmo
    val requestQueue = Volley.newRequestQueue(context)

}