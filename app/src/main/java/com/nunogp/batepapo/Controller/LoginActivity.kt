package com.nunogp.batepapo.Controller

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.nunogp.batepapo.R
import com.nunogp.batepapo.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //id
        loginSpinner.visibility = View.INVISIBLE
    }
    fun loginLoginBtnClicked(view: View){
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { isLoginSuccess ->
                if (isLoginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        }else{
            Toast.makeText(this, "Todos os campos têm de estar preenchidos.", Toast.LENGTH_LONG).show()
        }
    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    //mensagens de erro para registo e login
    fun errorToast(){
        Toast.makeText(this, "Algo correu mal, por favor tente outra vez.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    //ligar e desligar spinner
    fun enableSpinner(enable: Boolean){
        if (enable){
            loginSpinner.visibility = View.VISIBLE
            /*createUserBtn.isEnabled = false
            createAvatarImageView.isEnabled = false
            backgroundColorBtn.isEnabled = false*/
        }else{
            loginSpinner.visibility = View.INVISIBLE
            /*createUserBtn.isEnabled = true
            createAvatarImageView.isEnabled = true
            backgroundColorBtn.isEnabled = true*/
        }
        //se enable true fica false, se enable false fica true
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

     fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

}