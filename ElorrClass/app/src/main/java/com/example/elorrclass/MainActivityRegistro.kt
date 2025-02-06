package com.example.elorrclass

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.SocketManager
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityRegistro : AppCompatActivity() {

    private lateinit var socketManager: SocketManager
    private lateinit var user: TextView
    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var dni: TextView
    private lateinit var email: TextView
    private lateinit var telephone: TextView
    private lateinit var telephone2: TextView
    private lateinit var password: TextView
    private lateinit var confirmPassword: TextView
    private lateinit var usuarioRegister: Usuario
    private lateinit var trainingCycle: TextView
    private lateinit var courses: TextView
    private lateinit var intensiveDual: TextView
    private lateinit var imageBase64: String
    private val CAPTURA_IMAGEN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        user = findViewById(R.id.textView_UsuarioRegistro)
        name = findViewById(R.id.textView_Nombre)
        surname = findViewById(R.id.textView_Apellido)
        dni = findViewById(R.id.textView_DNI)
        email = findViewById(R.id.textView_Direccion)
        telephone = findViewById(R.id.textViewTelefono1)
        telephone2 = findViewById(R.id.textView_Telefono2)
        password = findViewById(R.id.textView_Contrasena)
        confirmPassword = findViewById(R.id.textView_ConfirmarContrasena)
        trainingCycle = findViewById(R.id.textView_CicloFormativo)
        courses = findViewById(R.id.textView_Curso)
        intensiveDual = findViewById(R.id.textView_DualIntensiva)

        socketManager = SocketManager(this)
        socketManager.connect()

        val username = intent.getStringExtra("username")
        if (username != null) {
            socketManager.getUserId(username)
        }

        findViewById<Button>(R.id.button_Volver).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_CambiarContrasena).setOnClickListener {
            val user = findViewById<TextView>(R.id.textView_UsuarioRegistro)
            val password = findViewById<TextView>(R.id.textView_Contrasena)
            val confirmPassword = findViewById<TextView>(R.id.textView_ConfirmarContrasena)

            if (password.text.isNotEmpty() && confirmPassword.text.isNotEmpty()) {
                if (password.text.toString() == "Elorrieta00" || confirmPassword.text.toString() == "Elorrieta00") {
                    Toast.makeText(
                        this,
                        "Se debe cambiar la contraseña por defecto",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (password.text.toString() == confirmPassword.text.toString()) {
                        socketManager.changePassword(user.text.toString(), password.text.toString())
                        Toast.makeText(
                            this,
                            "Contraseña cambiada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "Se debe ingresar ambas contraseñas", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        findViewById<Button>(R.id.button_Registro).setOnClickListener {
            if (user.text.isNotEmpty() && name.text.isNotEmpty() && surname.text.isNotEmpty() && dni.text.isNotEmpty() &&
                email.text.isNotEmpty() && telephone.text.isNotEmpty() && telephone2.text.isNotEmpty() &&
                password.text.isNotEmpty()
            ) {
                if (password.text.toString() == "Elorrieta00" || confirmPassword.text.toString() == "Elorrieta00") {
                    Toast.makeText(
                        this,
                        "La contraseña es por defecto debe cambiarla con el boton change pass",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (user.text != usuarioRegister.login && name.text != usuarioRegister.nombre && surname.text != usuarioRegister.apellidos &&
                        dni.text != usuarioRegister.dni && email.text != usuarioRegister.email && telephone.text != usuarioRegister.telefono1 &&
                        telephone2.text != usuarioRegister.telefono2
                    ) {
                        socketManager.register(
                            imageBase64,
                            user.text.toString(),
                            name.text.toString(),
                            surname.text.toString(),
                            dni.text.toString(),
                            email.text.toString(),
                            telephone.text.toString(),
                            telephone2.text.toString()
                        )
                    } else {
                        val intent = Intent(applicationContext, MainActivityLogin::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Todos los campos deben estar informados", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }

        findViewById<ImageView>(R.id.imageView_Avatar).setOnClickListener {
            val fotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(fotoIntent, CAPTURA_IMAGEN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURA_IMAGEN && resultCode == RESULT_OK) {
            val imagen = data?.extras?.get("data") as? Bitmap
            findViewById<ImageView>(R.id.imageView_Avatar).setImageBitmap(imagen)

            imageBase64 = bitmapToBase64(imagen)
        }
    }

    fun bitmapToBase64(bitmap: Bitmap?): String {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    fun preloadInfo(usuario: Usuario?) {
        if (usuario != null) {
            usuarioRegister = usuario
        }

        if (confirmPassword.text.isNotEmpty()) {
            confirmPassword.text = ""
        }

        if (usuario != null) {
            if (usuario.tipoUsuario == "Alumno") {
                if (usuario.login != null && usuario.nombre != null && usuario.apellidos != null &&
                    usuario.dni != null && usuario.email != null && usuario.telefono1 != null && usuario.telefono2 != null &&
                    usuario.password != null && usuario.cicloFormativo != null && usuario.curso != null && usuario.duales != null
                ) {
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
                } else {
                    Toast.makeText(this, "Información incompleta", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (usuario.login != null && usuario.nombre != null && usuario.apellidos != null &&
                    usuario.dni != null && usuario.email != null && usuario.telefono1 != null &&
                    usuario.telefono2 != null && usuario.password != null
                ) {
                    user.text = usuario.login
                    name.text = usuario.nombre
                    surname.text = usuario.apellidos
                    dni.text = usuario.dni
                    email.text = usuario.email
                    telephone.text = usuario.telefono1
                    telephone2.text = usuario.telefono2
                    password.text = usuario.password
                    trainingCycle.visibility = View.INVISIBLE
                    courses.visibility = View.INVISIBLE
                    intensiveDual.visibility = View.INVISIBLE
                } else {
                    Toast.makeText(this, "Información incompleta", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Error de usuario nulo", Toast.LENGTH_SHORT).show()
        }
    }

    fun registerAnswer() {
        val intent = Intent(applicationContext, MainActivityLogin::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}