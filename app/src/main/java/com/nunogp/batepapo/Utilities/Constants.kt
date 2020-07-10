package com.nunogp.batepapo.Utilities

//guardar URL para webb API   de heroku domain + pastas na API
const val  BASE_URL = "https://appchatbatepapo.herokuapp.com/v1/"
//para local host ip default para android emulators
//const val  BASE_URL = "http://10.0.2.2:3005/v1/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER = "${BASE_URL}user/byEmail/"

// broadcast constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"
