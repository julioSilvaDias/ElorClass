package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.elorrclass.logica.bbdd.AppDataBase
import com.example.elorrclass.logica.entity.Usuario
import com.example.elorrclass.socketIO.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivityLogin : AppCompatActivity() {

    private lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        socketManager = SocketManager(this)
        socketManager.connect()

        CoroutineScope(Dispatchers.IO).launch {
            val db =
                Room.databaseBuilder(applicationContext, AppDataBase::class.java, "user_database")
                    .fallbackToDestructiveMigration()
                    .build()

            val userDao = db.UsuarioDao()

            val lastUser = userDao.getUltimoUsuarioLogeado()

            runOnUiThread {
                lastUser?.let {
                    findViewById<EditText>(R.id.textView_IngresarUsuario).setText(it.username)
                    findViewById<EditText>(R.id.textView_IngresarClave).setText(it.password)
                }
            }
        }

        findViewById<Button>(R.id.button_OlvidarClave).setOnClickListener {
           //mostrarRestablecerClave()
        }

        findViewById<Button>(R.id.button_Registrar).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityRegistro::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_InicioSesion).setOnClickListener {
            val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
            val password = findViewById<EditText>(R.id.textView_IngresarClave).text.toString()
            /**
             * Si el usuario no pone nada, Se mandara el mensaje de que requiere
             * las credenciales
             */
            if (username.isEmpty()) {
                Toast.makeText(this, "Se necesita el login informado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            socketManager.loginUsuario(username, password)
        }
    }

    fun handleLoginResponse(response: String) {
        val cleanedResponse = response.trim()
        Log.d("LoginResponse", "Respuesta del servidor: $cleanedResponse")

        try {
            /**
             * Para parsear el Json recibido
             */
            val jsonResponse = JSONObject(cleanedResponse)
            val message = jsonResponse.getString("message")

            /**
             * El runOnUiThread es para asegurar de que las acciones
             * se ejecuten en el hilo principal
             */
            runOnUiThread {
                when (message) {
                    "Login correcto" -> {
                        val username =
                            findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
                        val password =
                            findViewById<EditText>(R.id.textView_IngresarClave).text.toString()

                        /**
                         * Para guardar el usuario en una base de datos en un hilo de fondo
                         */
                        CoroutineScope(Dispatchers.IO).launch {
                            saveUserToDatabase(username, password)
                        }

                        val intent = Intent(applicationContext, MainActivityPanel::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    }

                    "El Usuario no es alumno del centro." -> {
                        Toast.makeText(this, "El usuario no está registrado en el centro", Toast.LENGTH_SHORT).show()
                    }

                    "El usuario debe de registrarse." -> {
                        Toast.makeText(this, "El usuario debe registrarse para cambiar la contraseña", Toast.LENGTH_SHORT).show()
                        val username = findViewById<EditText>(R.id.textView_IngresarUsuario).text.toString()
                        val intent = Intent(applicationContext, MainActivityRegistro::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    }

                    "Login y/o Pass incorrecto" -> {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Error en el proceso de loginAndroid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("LoginResponse", "Error al procesar la respuesta del servidor", e)
            runOnUiThread {
                Toast.makeText(this, "Error inesperado al procesar la respuesta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*private fun mostrarRestablecerClave() {
        val dialogView = layoutInflater.inflate(R.layout.activity_resetear_password, null)
        val usernameEditText = dialogView.findViewById<EditText>(R.id.textView_RestablecerPassword)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Recuperar contraseña")
            .setMessage("Introduce tu nombre de usuario o correo electrónico para recuperar la contraseña.")
            .setView(dialogView)
            .setPositiveButton("Enviar") { _, _ ->
                val username = usernameEditText.text.toString()
                if (username.isNotEmpty()) {
                    enviarSolicitudParaRestablecerClave(username)
                } else {
                    Toast.makeText(this, "Por favor, ingresa tu usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        alertDialog.show()
    }*/

    private suspend fun saveUserToDatabase(username: String, password: String) {
        val user = Usuario(username = username, password = password, ultimoInicioSesion = true)

        val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()

        val userDao = db.UsuarioDao()

        userDao.resetUltimoInicioSesion()

        userDao.insert(user)
    }

    /*private fun enviarSolicitudParaRestablecerClave(username: String) {
        if (username.isEmpty()) {
            showToast("Por favor, ingresa tu usuario")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("PasswordReset", "Enviando solicitud para restablecer la clave para el usuario: $username")

                socketManager.resetearClave(username)

                Log.d("PasswordReset", "Solicitud de restablecimiento enviada correctamente para el usuario: $username")

                runOnUiThread {
                    showToast("Solicitud de recuperación enviada correctamente.")
                }
            } catch (e: Exception) {
                Log.e("PasswordReset", "Error al intentar enviar el correo de recuperación", e)
                runOnUiThread {
                    showToast("No se ha podido enviar el correo de recuperación.")
                }
            }
        }
    }*/

    /*fun handlePasswordResetResponse(response: String) {
        val cleanedResponse = response.trim()
        Log.d("PasswordResetResponse", "Respuesta del servidor: $cleanedResponse")

        try {
            val jsonResponse = JSONObject(cleanedResponse)
            val message = jsonResponse.getString("message")

            runOnUiThread {
                when (message) {
                    "Correo enviado correctamente" -> {
                        showToast("Se ha enviado un correo con la nueva clave.")
                    }

                    "Usuario no es alumno del centro" -> {
                        showToast("El usuario no está registrado en el centro.")
                    }

                    "Login no existe en BBDD remota" -> {
                        showToast("El login informado no existe en la base de datos.")
                    }

                    else -> {
                        showToast("No ha podido enviarse el correo. Intenta nuevamente.")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PasswordReset", "Error al procesar la respuesta del servidor", e)
            runOnUiThread {
                showToast("Ocurrió un error inesperado al procesar la respuesta.")
            }
        }
    }*/

    private fun navigateToRegistration(username: String) {
        val intent = Intent(applicationContext, MainActivityRegistro::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}
