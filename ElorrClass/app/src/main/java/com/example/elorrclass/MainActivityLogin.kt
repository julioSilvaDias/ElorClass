package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.elorrclass.logica.bbdd.AppDataBase
import com.example.elorrclass.logica.entity.Usuario
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivityLogin : AppCompatActivity() {

    private lateinit var mensajeSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        try {
            mensajeSocket = IO.socket("http://0.0.0.0:")
            mensajeSocket.connect()

            mensajeSocket.on("loginResponse") { args ->
                val response = args[0] as String
                runOnUiThread {
                    handleLoginResponse(response)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        findViewById<Button>(R.id.button_Registrar).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityRegistro::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_InicioSesion).setOnClickListener {
            val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
            val password = findViewById<EditText>(R.id.textView_IngresarClave).text.toString()
            sendLoginRequest(username, password)
            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleLoginResponse(response: String) {
        when (response) {
            "Login correcto" -> {
                val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
                val password = findViewById<EditText>(R.id.textView_IngresarClave).text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    saveUserToDatabase(username, password)
                }

                val intent = Intent(applicationContext, MainActivityPanel::class.java)
                startActivity(intent)
                finish()
            }
            "El Usuario no esta matriculado en el Centro" -> {
                Toast.makeText(this, "El usuario no está matriculado", Toast.LENGTH_SHORT).show()
            }

            "El usuario debe registrarse" -> {
                val intent = Intent(applicationContext, MainActivityRegistro::class.java)
                startActivity(intent)
            }

            "Login y/o Pass incorrecto" -> {
                // Mostrar mensaje de error
                Toast.makeText(this, "Login y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveUserToDatabase(username: String, password: String) {
        val user = Usuario(username = username, password = password)

        // Crear o acceder a la base de datos
        val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()

        val userDao = db.UsuarioDao()
        userDao.insert(user)
    }

    private fun sendLoginRequest(username: String, password: String) {
        val loginData = JSONObject()
        loginData.put("username", username.lowercase())
        loginData.put("password", password)
        mensajeSocket.emit("loginRequest", loginData)
    }
}
