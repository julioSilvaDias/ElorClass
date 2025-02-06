package com.example.elorrclass

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.elorrclass.util.ThemesUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivityPerfil : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchCambioTema: Switch
    private lateinit var spinnerChangeLanguage: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        spinnerChangeLanguage = findViewById(R.id.spinner)
        switchCambioTema = findViewById(R.id.switch_CambioTema)

        //CAMBIO DE IDIOMA
        val themesUtils = ThemesUtils()
        val languageCode = themesUtils.getLocale(this) ?: "en"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        //CAMBIO DE TEMA
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

        setUpSpinner()

    }

    private fun setUpSpinner() {
        val themesUtils = ThemesUtils()

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.changeLanguage,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChangeLanguage.adapter = adapter

        val currentLanguage = themesUtils.getLocale(this) ?: "en"
        val selectedPosition = when (currentLanguage) {
            "es" -> 1
            "eu" -> 2
            else -> 0
        }
        spinnerChangeLanguage.setSelection(selectedPosition)

        spinnerChangeLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    1 -> "es"
                    2 -> "eu"
                    else -> "en"
                }
                if (selectedLanguage != currentLanguage) {
                    themesUtils.setLocale(this@MainActivityPerfil, selectedLanguage)
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}