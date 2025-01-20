package com.example.elorrclass.socketIO

import android.media.metrics.Event
import android.util.Log
import com.example.elorrclass.MyApplication
import com.example.elorrclass.socketIO.config.Events
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketManager(private val activity: MyApplication){
    private val ipPort = "http://10.5.104.50:5000"
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
        }

    }

    fun loginUsuario(username: String, password: String) {
        val loginData = JSONObject().apply {
            put("login", username)
            put("password", password)
        }

        socket.emit(Events.ON_LOGIN.value, loginData)
        Log.d(tag, "login enviado: $loginData")
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
}
