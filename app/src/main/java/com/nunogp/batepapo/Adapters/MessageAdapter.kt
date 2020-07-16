package com.nunogp.batepapo.Adapters

import android.content.Context
import android.net.ParseException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nunogp.batepapo.Model.Message
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.UserDataService
import java.text.SimpleDateFormat
import java.util.*

//24 display messages

//2 <MessageAdapter.viewHolder
//3 class alt+enter :  implement members seleciona os 3
//4
class MessageAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.viewHolder>(){

    //1depois de .ViewHolder alt+enter add constroctor parameters
    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userImage = itemView?.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView?.findViewById<TextView>(R.id.timeStampLbl)
        val userName = itemView?.findViewById<TextView>(R.id.messageUserNameLbl)
        val messageBody = itemView?.findViewById<TextView>(R.id.messageBodyLbl)

      fun bindMessage(context: Context, message: Message){
          val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
          userImage?.setImageResource(resourceId)
          userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
          userName?.text = message.userName
          //25 date formatter comentei substitui por fun
          //timeStamp?.text = message.timeStamp
          timeStamp?.text = returnDateString(message.timeStamp)
          messageBody?.text = message.message


      }
    //25 date formatter
    fun returnDateString(isoString: String): String{
        //2017-09-11T01:16:13.858Z como está agora -> Monday 4:35 PM
        //pattern o que está em cima
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var convertDate = Date()
        try {
            //parse string to date object
            convertDate = isoFormatter.parse(isoString)
        }catch (e: ParseException){
            Log.d("PARSE", "Can not parse date")
        }
        //fazer outro patern format
        val outDateString = SimpleDateFormat("E d L y, h:mm a ", Locale.getDefault())
        // format date:date
        return outDateString.format(convertDate)

    }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return  messages.count()
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        //depois de fazer corpo de inner class viewHolder
        holder?.bindMessage(context, messages[position])


    }

}