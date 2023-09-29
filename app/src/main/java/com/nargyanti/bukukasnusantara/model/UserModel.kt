package com.nargyanti.bukukasnusantara.model

class UserModel {
    var id : Int = 0
    var username : String = ""
    var password : String = ""

    fun setUserData(id: Int, password: String) {
        this.id = id
        this.password = password
    }
}