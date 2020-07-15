package com.nunogp.batepapo.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.nunogp.batepapo.Controller.App
import com.nunogp.batepapo.Model.Channel
import com.nunogp.batepapo.Model.Message
import com.nunogp.batepapo.Utilities.URL_GET_CHANNELS
import com.nunogp.batepapo.Utilities.URL_GET_MESSAGES
import org.json.JSONException

//19 down channels
object MessageService {
    //                    Channel class model  vazio
    val channels = ArrayList<Channel>()
    //22 send receive message
    val messages = ArrayList<Message>()

//20shared preferences não precisa de context
    fun getChannels(/*context: Context,*/ complete: (Boolean) -> Unit){
        val channelsRequest = object : JsonArrayRequest(GET, URL_GET_CHANNELS, null, Response.Listener {response ->
            try {
                  for (x in 0 until response.length()){
                    val  channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val chanDesc = channel.getString("description")
                    val channelId = channel.getString("_id")
                      //          model    criar object
                    val newChannel = Channel(name, chanDesc, channelId)
                  //adicionar no array
                    this.channels.add(newChannel)
                 }
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener{error ->
            Log.d("ERROR", "Não foi possível devolver canais, tente outra vez.")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                //           k v
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return  headers
            }
        }
        //20 shared preferences
        App.prefs.requestQueue.add(channelsRequest)
        //Volley.newRequestQueue(context).add(channelsRequest)
    }
// 23 downloading messages
    fun getMessages(channelId: String, complete: (Boolean) -> Unit){
        val url = "$URL_GET_MESSAGES$channelId"

        val messagesRequest = object :JsonArrayRequest(Method.GET, url, null, Response.Listener {response ->
            clearMessages()
            try {//se não for response pode ser it
                for (x in 0 until response.length()){
                    val message = response.getJSONObject(x)//name = postman
                    val messageBody = message.getString("messageBody")
                    val userId = message.getString("userId")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timeStamp = message.getString("timeStamp")

                    //Message model,
                    val newMessage = Message(messageBody, userName, channelId, userAvatar,
                        userAvatarColor, id, timeStamp)
                    this.messages.add(newMessage)
                }
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Não foi possível devolver canais, tente outra vez.")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                //           k v
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return  headers
            }

        }
        App.prefs.requestQueue.add(messagesRequest)
    }

    // 23 downloading messages
    // esvaziar array ao mudar de canal e no logout
    fun clearMessages(){
        messages.clear()
    }

    // 23 downloading messages
    //esvaziar array channels
    fun clearChannels (){
        channels.clear()
    }

}