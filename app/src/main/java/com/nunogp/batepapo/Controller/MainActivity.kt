git branchpackage com.nunogp.batepapo.Controller

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
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isEmpty
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nunogp.batepapo.Model.Channel
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.AuthService
import com.nunogp.batepapo.Services.MessageService
import com.nunogp.batepapo.Services.UserDataService
import com.nunogp.batepapo.Utilities.BROADCAST_USER_DATA_CHANGE
import com.nunogp.batepapo.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    //socket     uri string
    val socket = IO.socket(SOCKET_URL)

    //19 down chanels                        model
    lateinit var channelAdapter: ArrayAdapter<Channel>
    //21 select channels model null pode nao haver e antes de fazer login
    var selectedChannel: Channel? = null

    //19 down chanels
    private fun setupAdapters(){
       //
        //if (MessageService.channels.isEmpty()) {
            channelAdapter =  ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
            //view ID
            channel_list.adapter = channelAdapter
        //}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //18 socket.On string emiter oi.emit event, listener
        socket.connect()
        socket.on("channelCreated", onNewCannel)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        //down chanels
        setupAdapters()

            //20 shared preferences id listview int long select channnel
         channel_list.setOnItemClickListener { _, _, i, _ ->
            selectedChannel = MessageService.channels[i]
            //esconder menu
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()

        }

        //20 shared preferences
        if (App.prefs.isLoggedIn){
           AuthService.findUserByEmail(this){}
        }

        //receive broadcast depois de down channels duplica
        //LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
          //  BROADCAST_USER_DATA_CHANGE))
    }

    //17 socket
    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE))
        //socket.connect()
        super.onResume()
    }
    //17 socket
    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }
    //create receiver broadcast receiver
    private val userDataChangeReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            //update UI nav header in login ou create user
            if (App.prefs.isLoggedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                //em vez de dizer entrar depois de passa a ser sair
                loginBtnNavHeader.text = "Sair"

                //down chanels 1  20shared preferences não precisa de context
                MessageService.getChannels(/*context*/){complete ->
                    if (complete) {
                        //21 channel selection if default apresenta 1º channel no array
                        if (MessageService.channels.count() > 0 ){
                                //mainChannelName.text = "Selecione um canal"
                                selectedChannel = MessageService.channels[0]
                                //reload if datachange
                                channelAdapter.notifyDataSetChanged()
                                updateWithChannel()
                        }

                    }
                }
            }
        }
    }

    // 21 channel selection textview com nome de channel
    fun updateWithChannel(){
       //id textview
        mainChannelName.text = "#${selectedChannel?.name}"
        //download messages for channels
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View){

        if (App.prefs.isLoggedIn){
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
        if (App.prefs.isLoggedIn){
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)// 20shared preferences _
                    .setPositiveButton("Adicionar"){/*dialogInterface*/_, /*i*/_ ->
                        //perform logic when click
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                        val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)
                        val channelName = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()

                        //create channel with the channel name and description
                        //17 socket emit enviar event newChannel, info, info como API code src\index.js
                        socket.emit("newChannel",channelName, channelDesc )

                    } //20shared preferences substitui  _
                .setNegativeButton("Cancelar"){/*dialogInterface*/_, /*i*/_ ->
                    //cancel and close the dialog
                }
                .show()
        }else{
            Toast.makeText(this, "Tem de entrar primeiro.", Toast.LENGTH_SHORT).show()
        }
    }

    //18 socket.On create emit listener worker thread
    private val onNewCannel = Emitter.Listener { args ->
        // action unit
        runOnUiThread {
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String
            //           class    model
            val newChannel = Channel(channelName, channelDescription, channelId )
            //criar object   no array key.value
            MessageService.channels.add(newChannel)
            //19 down chanels update reload view
            channelAdapter.notifyDataSetChanged()
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
