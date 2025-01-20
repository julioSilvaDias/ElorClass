package com.example.elorrclass.socketIO

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketManager {
    private val ipPort = "http://0.0.0.0:5000"
    private val socket: Socket = IO.socket(ipPort)
    private var tag = "socket.io"

    init{
        socket.on(Socket.EVENT_CONNECT){
            Log.d(tag, "Conneted...")
        }

        socket.on(Socket.EVENT_DISCONNECT){
            Log.d(tag, "Disconnected")
        }

        socket.on("custom_event") { args ->
            val data = args[0] as JSONObject
            println("Evento recibido: $data")
        }
    }
}