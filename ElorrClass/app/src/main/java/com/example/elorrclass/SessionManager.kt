package com.example.elorrclass

object SessionManager {
    private var username: String? = null

    fun login(username: String){
        this.username = username
    }

    fun logout(){
        this.username = null
    }

    fun getUsername(): String?{
        return username
    }
}