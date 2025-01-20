package com.example.elorrclass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivityPerfil : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        val switchCambioTema: Switch = findViewById(R.id.switch_CambioTema)

        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        switchCambioTema.isChecked = isDarkMode

        switchCambioTema.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                recreate()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        findViewById<Button>(R.id.button_VolverPanel).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()
        }
    }
}