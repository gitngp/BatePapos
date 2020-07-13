package com.nunogp.batepapo.Controller

import android.app.Application
import com.nunogp.batepapo.Utilities.SharedPrefs
//20 sahred preferences
class App: Application() {

    companion object{
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }

}