package com.nunogp.batepapo.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.AuthService
import com.nunogp.batepapo.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

     //nome da imagem se user não quiser outra fica este por defeito
    var userAvatar = "profiledefault"
    //rgb
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        //id progressbar começar invisivel
        createSpinner.visibility = View.INVISIBLE
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
        enableSpinner(true)
        //user/add
        //ids textfield
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        //verificar se textfield tem text por password maior que 6
        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            //20shared preferences não precisa de context
            AuthService.registerUser(/*this,*/email, password){registerSuccess ->
                if (registerSuccess) {
                    //20shared preferences não precisa de context
                    AuthService.loginUser(/*this,*/email, password){loginSuccess ->
                        if (loginSuccess){
                            //println(AuthService.authToken)
                            //println(AuthService.userEmail)
                            //20shared preferences não precisa de context
                            AuthService.createUser(/*this,*/ userName, email, userAvatar, avatarColor){createSuccess ->
                                if (createSuccess){

                                    //enviar local broeadcast manager  constant
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    //dismiss activity
                                    finish()
                                }else{
                                    errorToast()
                                }
                            }
                        }else{
                            errorToast()
                        }

                    }
                }else{
                    errorToast()
                }
            }

        }else{
            Toast.makeText(this, "Todos os campos têm de estar preenchidos.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }
    //mensagens de erro para registo e login
    fun errorToast(){
        Toast.makeText(this, "Algo correu mal, por favor tente outra vez.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    //ligar e desligar spinner
    fun enableSpinner(enable: Boolean){
        if (enable){
            createSpinner.visibility = View.VISIBLE
            /*createUserBtn.isEnabled = false
            createAvatarImageView.isEnabled = false
            backgroundColorBtn.isEnabled = false*/
        }else{
            createSpinner.visibility = View.INVISIBLE
            /*createUserBtn.isEnabled = true
            createAvatarImageView.isEnabled = true
            backgroundColorBtn.isEnabled = true*/
        }
        //se enable true fica false, se enable false fica true
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}