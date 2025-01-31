package com.example.elorrclass.socketIO

import android.app.Activity
import android.util.Log
import com.example.elorrclass.MainActivityLogin
import com.example.elorrclass.MainActivityRegistro
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.config.Events
import com.example.elorrclass.socketIO.model.MessageInput
import com.example.elorrclass.socketIO.model.UserPass
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketManager(private val activity: Activity){
    private val ipPort = "http://10.5.104.30:5000"
    private val socket: Socket = IO.socket(ipPort)
    private var tag = "socket.io"

    init{
        socket.on(Socket.EVENT_CONNECT){
            Log.d(tag, "Conneted...")
        }

        socket.on(Socket.EVENT_DISCONNECT){
            Log.d(tag, "Disconnected")
        }

        socket.on(Events.ON_LOGIN_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d("Socket", "Mensaje recibido: $message")

            (activity as? MainActivityLogin)?.handleLoginResponse(message)
        }

        socket.on("loginResponse") { args ->
            val response = args[0] as JSONObject
            val success = response.getBoolean("success")
            val message = response.getString("message")

            Log.d("Socket", "Respuesta del servidor: Éxito: $success, Mensaje: $message")

            (activity as? MainActivityLogin)?.handleLoginResponse(message)
        }

        socket.on(Events.ON_GET_USER_ID_ANSWER.value){ args->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d("Socket", "Mensaje recibido: $message")

            val gson = Gson()
            val usuario = gson.fromJson(message, Usuario::class.java)
            println(usuario)
            (activity as MainActivityRegistro).preloadInfo(usuario)
        }

        socket.on(Events.ON_GET_HORARIO_ANSWER.value){ args->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d(tag, "mesaje recibido: $message")
        }

        socket.on(Events.ON_CHANGE_PASSWORD_ANSWER.value){ args->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d(tag, "mesaje recibido: $message")

            val gson = Gson()
            val usuario = gson.fromJson(message, Usuario::class.java)
            println(usuario)
            (activity as MainActivityRegistro).preloadInfo(usuario)
        }

    }
    fun getHorario(userId : Int?){
        val message = MessageInput(userId.toString())
        socket.emit(Events.ON_GET_HORARIO.value, Gson().toJson(message))
        Log.d (tag, "datos enviados: -> $message")
    }
    fun getUserId(username: String){
        val userPass = UserPass(username, "")
        socket.emit(Events.ON_GET_USER_ID.value, Gson().toJson(userPass))
        Log.d(tag, "Username enviado: $userPass")
    }
    fun loginUsuario(username: String, password: String) {
        //val loginData = JSONObject().apply {
        //    put("login", username)
        //   put("password", password)
        //}

        val userPass = UserPass(username, password)
        socket.emit(Events.ON_LOGIN.value, Gson().toJson(userPass))
        Log.d(tag, "Login enviado: $userPass")
    }

    fun connect(){
        socket.connect()
        Log.d(tag, "connecting to server....")
    }

    fun disconnect() {
        socket.disconnect()
        Log.d(tag, "Disconnecting from server...")
    }

    fun isConnected(): Boolean {
        return socket.connected().also {
            Log.d(tag, if (it) "Socket is connected" else "Socket is not connected")
        }}

    fun changePassword(username: String, password: String) {
        val userPass = UserPass(username, password)
        socket.emit(Events.ON_CHANGE_PASSWORD.value, Gson().toJson(userPass))
        Log.d (tag, "datos enviados: -> $userPass")
    }
}