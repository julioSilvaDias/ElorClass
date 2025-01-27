package com.example.elorrclass

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.SocketManager

class MainActivityRegistro : AppCompatActivity() {

    private lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        socketManager = SocketManager(this)
        socketManager.connect()

        val username = intent.getStringExtra("username")
        if (username != null) {
            socketManager.getUserId(username)
        }

        findViewById<Button>(R.id.button_Volver).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.button_CambiarContrasena).setOnClickListener {

        }

        findViewById<Button>(R.id.button_Registro).setOnClickListener {

        }
    }

    fun preloadInfo(usuario: Usuario?) {
        val user = findViewById<EditText>(R.id.textView_UsuarioRegistro)
        val name = findViewById<EditText>(R.id.textView_Nombre)
        val surname = findViewById<EditText>(R.id.textView_Apellido)
        val dni = findViewById<EditText>(R.id.textView_DNI)
        val address = findViewById<EditText>(R.id.textView_Direccion)
        val telephone = findViewById<EditText>(R.id.textViewTelefono1)
        val telephone2 = findViewById<EditText>(R.id.textView_Telefono2)
        val password = findViewById<EditText>(R.id.textView_Contrasena)
        val confirmPassword = findViewById<EditText>(R.id.textView_ConfirmarContrasena)
        val trainingCycle = findViewById<TextView>(R.id.textView_CicloFormativo)
        val courses = findViewById<TextView>(R.id.textView_Curso)
        val intensiveDual = findViewById<TextView>(R.id.textView_DualIntensiva)
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}