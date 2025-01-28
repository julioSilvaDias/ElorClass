package com.example.elorrclass

import android.os.Bundle
import android.widget.Button
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
        val user = findViewById<TextView>(R.id.textView_UsuarioRegistro)
        val name = findViewById<TextView>(R.id.textView_Nombre)
        val surname = findViewById<TextView>(R.id.textView_Apellido)
        val dni = findViewById<TextView>(R.id.textView_DNI)
        val email = findViewById<TextView>(R.id.textView_Direccion)
        val telephone = findViewById<TextView>(R.id.textViewTelefono1)
        val telephone2 = findViewById<TextView>(R.id.textView_Telefono2)
        val password = findViewById<TextView>(R.id.textView_Contrasena)
        //val confirmPassword = findViewById<TextView>(R.id.textView_ConfirmarContrasena)
        val trainingCycle = findViewById<TextView>(R.id.textView_CicloFormativo)
        val courses = findViewById<TextView>(R.id.textView_Curso)
        val intensiveDual = findViewById<TextView>(R.id.textView_DualIntensiva)

        if (usuario != null) {
            user.text = usuario.login
            name.text = usuario.nombre
            surname.text = usuario.apellidos
            dni.text = usuario.dni
            email.text = usuario.email
            telephone.text = usuario.telefono1
            telephone2.text = usuario.telefono2
            password.text = usuario.password
            trainingCycle.text = usuario.cicloFormativo
            courses.text = usuario.curso.toString()
            intensiveDual.text = usuario.duales.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}