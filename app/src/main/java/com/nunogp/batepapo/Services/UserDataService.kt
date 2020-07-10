package com.nunogp.batepapo.Services

import android.graphics.Color
import java.util.*

object UserDataService {
    //keys response add User postman
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun returnAvatarColor(components: String): Int {
        // ex em db net [0.7843137254901961, 0.19607843137254902, 0.03529411764705882, 1]

        //ficar so com numeros
        // 0.7843137254901961 0.19607843137254902 0.03529411764705882 1
        val strippedColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")

        //valores entre 255
        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }

    fun logout(){
        var id = ""
        var avatarColor = ""
        var avatarName = ""
        var email = ""
        var name = ""
        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedIn = false
    }

}