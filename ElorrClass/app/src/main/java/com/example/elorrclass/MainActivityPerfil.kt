package com.example.elorrclass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.elorrclass.util.ThemesUtils
import java.util.Locale

class MainActivityPerfil : AppCompatActivity() {

    private lateinit var spinnerChangeLanguage: Spinner

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        spinnerChangeLanguage = findViewById(R.id.spinner)
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

    private fun setUpSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.changeLanguage,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerChangeLanguage.adapter = adapter

        /*spinnerChangeLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = if (position == 0) "en" else "es"
                val currentLanguage = Locale.getDefault().language
                if (selectedLanguage != currentLanguage) {
                    ThemesUtils.setLocale(this@MainActivityProfile, selectedLanguage)
                    recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }*/
    }

}