package com.example.elorrclass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import bbdd.pojos.Reunion
import com.example.elorrclass.socketIO.SocketManager

class MainActivityReuniones : AppCompatActivity() {
    private val socketManager= SocketManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_reuniones)
        val username = intent.getStringExtra("username")
        val userId = intent.getIntExtra("userId", 0)
        socketManager.connect()
        socketManager.getReuniones(userId)

    }

    fun handleReunionResponse(reuniones: List<Reunion>){
        println(reuniones)
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}