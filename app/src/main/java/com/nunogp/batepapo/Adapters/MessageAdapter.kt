package com.nunogp.batepapo.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nunogp.batepapo.Model.Message
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.UserDataService
import java.util.ArrayList

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
          timeStamp?.text = message.timeStamp
          messageBody?.text = message.message


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