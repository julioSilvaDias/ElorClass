package com.example.elorrclass.socketIO

import CustomSqlDateAdapter
import android.app.Activity
import android.util.Log
import bbdd.pojos.Reunion
import com.example.elorrclass.MainActivityCursosExternos
import com.example.elorrclass.MainActivityLogin
import com.example.elorrclass.MainActivityPerfil
import com.example.elorrclass.MainActivityRegistro
import com.example.elorrclass.MainActivityPanel
import com.example.elorrclass.MainActivityReuniones
import com.example.elorrclass.adapter.TimestampAdapter
import com.example.elorrclass.pojos.CursosExternos
import com.example.elorrclass.pojos.Horario
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.config.Events
import com.example.elorrclass.socketIO.model.MessageInput
import com.example.elorrclass.socketIO.model.UpdateUser
import com.example.elorrclass.socketIO.model.UserPass
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.sql.Date
import java.sql.Timestamp

class SocketManager(private val activity: Activity) {
    private val ipPort = "http://10.5.104.26:5000"
    private val socket: Socket = IO.socket(ipPort)
    private var tag = "socket.io"

    init {
        socket.on(Socket.EVENT_CONNECT) {
            Log.d(tag, "Conneted...")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
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

        socket.on(Events.ON_GET_USER_ID_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d("Socket", "Mensaje recibido: $message")

            val gson = Gson()
            val usuario = gson.fromJson(message, Usuario::class.java)
            (activity as? MainActivityPanel)?.handleUserResponse(usuario)
            println(usuario)
            (activity as MainActivityRegistro).preloadInfo(usuario)
            (activity as MainActivityPerfil).changePassword(usuario)
        }

        socket.on(Events.ON_GET_HORARIO_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")

            val horarios = Gson().fromJson(message, Array<Horario>::class.java).toList()
            println(horarios)
            (activity as? MainActivityPanel)?.handleHorarioResponse(horarios)
        }

        socket.on(Events.ON_GET_MEETINGS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d(tag, message)

            val gson = GsonBuilder()
                .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())
                .create()

            val listType = object : TypeToken<List<Reunion>>() {}.type
            val reuniones = gson.fromJson<List<Reunion>>(message, listType)
            (activity as? MainActivityReuniones)?.handleReunionResponse(reuniones)
        }

        socket.on(Events.ON_GET_ALL_CURSOS_ANSWER.value) { args ->
            val reponse = args[0] as JSONObject
            val message = reponse.getString("message")
            Log.d(tag, message)

            val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, CustomSqlDateAdapter())
                .create()

            val listType = object : TypeToken<List<CursosExternos>>() {}.type
            val cursosExternos = gson.fromJson<List<CursosExternos>>(message, listType)
            println(cursosExternos)
            (activity as? MainActivityCursosExternos)?.handleCursosExternosResponse(cursosExternos)

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

        socket.on(Events.ON_REGISTER_ANSWER.value){ args->
            val response = args[0] as JSONObject
            val message = response.getString("message")
            Log.d(tag, "mesaje recibido: $message")

            (activity as MainActivityRegistro).registerAnswer()
        }

    }

    fun getHorario(userId: Int?) {
        val message = MessageInput(userId.toString())
        socket.emit(Events.ON_GET_HORARIO.value, Gson().toJson(message))
        Log.d(tag, "datos enviados: -> $message")
    }

    fun getUserId(username: String) {
        val userPass = UserPass(username, "")
        socket.emit(Events.ON_GET_USER_ID.value, Gson().toJson(userPass))
        Log.d(tag, "Username enviado: $userPass")
    }

    fun loginUsuario(username: String, password: String) {
        val userPass = UserPass(username, password)
        socket.emit(Events.ON_LOGIN.value, Gson().toJson(userPass))
        Log.d(tag, "Login enviado: $userPass")
    }

    fun getReuniones(userId: Int?) {
        val message = MessageInput(userId.toString())
        socket.emit(Events.ON_GET_MEETINGS.value, Gson().toJson(message))
        Log.d(tag, "Id enviado: $message")
    }

    fun getAllCursos() {
        val message = MessageInput("")
        socket.emit(Events.ON_GET_ALL_CURSOS.value, Gson().toJson(message))
    }

    fun connect() {
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

    fun register(
        user: String,
        name: String,
        surname: String,
        dni: String,
        email: String,
        telefono1: String,
        telefono2: String,
    ) {
        val updateUser = UpdateUser(user, name, surname, dni, email, telefono1, telefono2)
        socket.emit(Events.ON_REGISTER.value, Gson().toJson(updateUser))
        Log.d (tag, "datos enviados: -> $updateUser")
    }
}