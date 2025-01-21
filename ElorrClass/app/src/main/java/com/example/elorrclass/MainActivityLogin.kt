package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.socketIO.SocketManager

class MainActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.button_Registrar).setOnClickListener{
            val intent = Intent(applicationContext, MainActivityRegistro::class.java)
            startActivity(intent)
            finish()
        }

        val socketManager = SocketManager(this)

        findViewById<Button>(R.id.button_InicioSesion).setOnClickListener {
            /*val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()*/

            val password = "hola"
            val username = "hola"

            socketManager.loginUsuario(username, password)
        }

    }
}