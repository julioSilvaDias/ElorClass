package com.example.elorrclass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.SocketManager
import com.example.elorrclass.util.ThemesUtils
import org.w3c.dom.Text
import java.util.Locale

class MainActivityPerfil : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchCambioTema: Switch
    private lateinit var spinnerChangeLanguage: Spinner
    private lateinit var socketManager: SocketManager
    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        socketManager = SocketManager(this)
        socketManager.connect()

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

        socketManager.getUserId("alumno1")

        findViewById<Button>(R.id.button_VolverPanel).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()
        }

        /*val username = intent.getStringExtra("username")
        if (username != null) {
            socketManager.getUserId(username)
        }*/

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

    fun changePassword(usuario: Usuario?) {
        if (usuario != null) {
            findViewById<Button>(R.id.button_CambioDeClave).setOnClickListener {
                val currentPassword = findViewById<TextView>(R.id.textView_IngresarClaveActual)
                val newPassword = findViewById<TextView>(R.id.textView_IngresarNuevaClave)
                val confirmPassword = findViewById<TextView>(R.id.textView_ConfirmarNuevaClave)
                if (currentPassword.text.isNotEmpty() && newPassword.text.isNotEmpty() && confirmPassword.text.isNotEmpty()) {
                    if (currentPassword.text.toString() != usuario.password) {
                        Toast.makeText(this, "No coincide la contraseña actual", Toast.LENGTH_SHORT).show()
                    } else {
                        if (newPassword.text.toString()== usuario.password) {
                            Toast.makeText(this, "La contraseña nueva es igual que la anterior", Toast.LENGTH_SHORT).show()
                        } else if (newPassword.text.toString() == confirmPassword.text.toString()) {
                            socketManager.changePassword(usuario.login.toString(), newPassword.text.toString())
                            Toast.makeText(this, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show()
                            currentPassword.text = ""
                            newPassword.text =""
                            confirmPassword.text = ""
                        } else {
                            Toast.makeText(this, "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Se debe ingresar las contraseñas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}