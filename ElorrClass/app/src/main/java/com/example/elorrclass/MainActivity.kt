package com.example.elorrclass

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.elorrclass.socketIO.SocketManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var socketManager : SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val socketManager = (application as MyApplication).socketManager

        /**
         * Implementacion de la animacion del logo y
         * verifica si el archivo existe
         */
        val videoView : VideoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.logomovimiento

        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)
        videoView.start()

        if (uri != null) {
            videoView.setVideoURI(uri)
            videoView.start()
        } else {
            Toast.makeText(this, "El archivo de video no se pudo cargar", Toast.LENGTH_SHORT).show()
        }

        /**
         * Verificar si hay conexion con el servidor
         */
        if (socketManager.isConnected()) {
            val testData = JSONObject().apply {
                put("message", "Comunicación desde Android")
            }

        } else {
            println("El servidor no está conectado.")
        }

        val testData = JSONObject().apply {
            put("message", "Comunicación desde Android")
        }


        /**
         * Comprobar la conectividad
         */
        checkConectividadAInternet()

    }

    private fun checkConectividadAInternet() {

        if (isConectadoARed()) {
            irALogin()
        } else {
           mostrarMensajeError()
           reintentarConexion()
        }

    }

    private fun isConectadoARed(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun irALogin() {
        Toast.makeText(this, "Conexión establecida correctamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivityLogin::class.java)
        startActivity(intent)
        finish()
    }

    private fun mostrarMensajeError(){
        Toast.makeText(this, "No estás conectado a internet, verifica tu conexión", Toast.LENGTH_SHORT).show()
    }

    private fun reintentarConexion() {
        lifecycleScope.launch {
            var intentos = 0
            val maximosIntentos = 5
            while (!isConectadoARed() && intentos < maximosIntentos) {
                delay(5000)
                intentos++
                Toast.makeText(this@MainActivity, "Reintentando conectarse a la red ($intentos/$maximosIntentos)", Toast.LENGTH_SHORT).show()
            }

            if (isConectadoARed()) {
                irALogin()
            } else {
                Toast.makeText(this@MainActivity, "No se ha podido conectar después de $maximosIntentos intentos", Toast.LENGTH_LONG).show()
            }
        }
    }

  /*  override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }*/
}