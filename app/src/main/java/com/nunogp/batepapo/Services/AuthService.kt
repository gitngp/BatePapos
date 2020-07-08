package com.nunogp.batepapo.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nunogp.batepapo.Utilities.URL_CREATE_USER
import com.nunogp.batepapo.Utilities.URL_LOGIN
import com.nunogp.batepapo.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

//operaçoes envolvam autorizaçao
object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    //context para volley                                              se registration foi sucess ou não devolve unit
    fun registerUser(context:Context, email: String, password: String, complete:(Boolean)-> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
//                           volley
        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener {response ->
            println(response)
            complete(true)
        },Response.ErrorListener {error ->
            Log.d("ERROR", "Não foi possivel registar:$error")
            complete(false)
        } ){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
    fun loginUser(context: Context, email: String,password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
//                                 volley
        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {response ->
            //this is where parse the json object received
            try {
                                                // key name in response
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLoggedIn = true
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            //this is where we deal with our error
            Log.d("ERROR", "Não foi possivel entrar:$error")
            complete(false)

        } ){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avtarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener {response ->
            try {
                //guardar json object ordem == postman user/add
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {error ->

            Log.d("ERROR", "Não foi possivel registar:$error")
            complete(false)
        }){
           override fun getBodyContentType(): String {
              return "application/json; charset=utf-8"
          }
           override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createRequest)
    }

}