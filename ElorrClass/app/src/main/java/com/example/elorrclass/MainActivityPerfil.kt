package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivityPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        findViewById<Button>(R.id.button_VolverPanel).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()
        }

    }
}