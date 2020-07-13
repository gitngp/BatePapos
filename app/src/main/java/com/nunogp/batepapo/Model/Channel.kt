package com.nunogp.batepapo.Model

class Channel (val name: String, val description: String, val id: String){
    //display como em coder swag #usasse em chat
    override fun toString(): String {
        return "#$name"
    }






}