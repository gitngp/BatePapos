package com.nunogp.batepapo.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
//import android.support.design.widget.Snackbar
//import android.support.design.widget.NavigationView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.AuthService
import com.nunogp.batepapo.Services.UserDataService
import com.nunogp.batepapo.Utilities.BROADCAST_USER_DATA_CHANGE
import com.nunogp.batepapo.Utilities.SOCKET_URL
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    //socket     uri string
    val socket = IO.socket(SOCKET_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //receive broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))

    }

    //17 socket
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE))
        socket.connect()
    }
    //17 socket
    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onPause()
    }
    //17 socket
    override fun onDestroy() {
        socket.disconnect()
        super.onDestroy()
    }

    //create receiver broadcast receiver
    private val userDataChangeReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //update UI nav header in login ou create user
            if (AuthService.isLoggedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                //em vez de dizer entrar depois de passa a ser sair
                loginBtnNavHeader.text = "Sair"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View){

        if (AuthService.isLoggedIn){
            //logout sair
            UserDataService.logout()
            userNameNavHeader.text = "Nome"
            userEmailNavHeader.text = "Email"
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text = "Entrar"
        }else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View){
        if (AuthService.isLoggedIn){
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                    .setPositiveButton("Adicionar"){dialogInterface, i ->
                        //perform logic when click
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                        val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)
                        val channelName = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()

                        //create channel with the channel name and description
                        //17 socket emit enviar event newChannel, info, info como API code src\index.js
                        socket.emit("newChannel",channelName, channelDesc )


                    }
                .setNegativeButton("Cancelar"){dialogInterface, i ->
                    //cancel and close the dialog

                }
                .show()
        }else{
            Toast.makeText(this, "Tem de entrar primeiro.", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMsgBtnClicked(view: View){
        hideKeyboard()
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

}
