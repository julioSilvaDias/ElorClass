package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.elorrclass.logica.bbdd.AppDataBase
import com.example.elorrclass.logica.entity.Usuario
import com.example.elorrclass.socketIO.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityLogin : AppCompatActivity() {

    private lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        socketManager = SocketManager(this)

        socketManager.connect()

        findViewById<Button>(R.id.button_Registrar).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityRegistro::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_InicioSesion).setOnClickListener {
            val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
            val password = findViewById<EditText>(R.id.textView_IngresarClave).text.toString()
            socketManager.loginUsuario(username, password)
        }
    }

    fun handleLoginResponse(response: String) {
        val cleanedResponse = response.trim()  // Eliminar espacios extra antes y después

        Log.d("LoginResponse", "Mensaje recibido: '$cleanedResponse'")  // Verifica lo que recibes

        if (cleanedResponse == "Login correcto") {
            val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
            val password = findViewById<EditText>(R.id.textView_IngresarClave).text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                saveUserToDatabase(username, password)
            }

            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            startActivity(intent)
            finish()
        } else if (cleanedResponse == "Login y/o Pass incorrecto") {
            runOnUiThread {
                Toast.makeText(this, "Login y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        } else {
            runOnUiThread {
                Toast.makeText(this, "Error en el proceso de login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveUserToDatabase(username: String, password: String) {
        val user = Usuario(username = username, password = password)

        val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()

        val userDao = db.UsuarioDao()
        userDao.insert(user)
    }
    /*
    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
    */
}
