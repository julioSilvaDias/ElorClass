package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivityPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel)

        findViewById<ImageButton>(R.id.imageButton_Perfil).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityPerfil::class.java)
            startActivity(intent)
            finish()
        }

    }
}