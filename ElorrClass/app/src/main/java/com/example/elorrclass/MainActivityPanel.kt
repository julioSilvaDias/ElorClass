package com.example.elorrclass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.socketIO.SocketManager

class MainActivityPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel)

        val socketManager = SocketManager(this)
        socketManager.connect()

        //val userName = SessionManager.getUsername()
        val userName = "alumno1"
        socketManager!!.getUserId(userName)
    }
}