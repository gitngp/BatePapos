package com.nunogp.batepapo.Controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nunogp.batepapo.R
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

     //nome da imagem se user não quiser outra fica este por defeito
    var userAvatar = "profileDefault"
    //rgb
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun generateUserAvatar(view: View){
        //Random java.util
        val random = Random()
        val color = random.nextInt(2)
        //images ate 27
        val avatar = random.nextInt(28)

        if (color ==0){
            userAvatar = "light$avatar"
        }else{
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        //btn id
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View){
        val random = Random()
        //gerar cor rgb
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        //id
        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))
        //passar para valor entre 0 e 1 para por em avatarColor
        //mandar para database e API dar para outros aparelhos
        val saveR = r.toDouble()/255
        val saveG = g.toDouble()/255
        val saveB = b.toDouble()/255

        avatarColor = "[$saveR, $saveG, $saveB, 1]"
        //println("\n ${avatarColor}!")
    }

    fun createUserClicked(view: View){

    }
}