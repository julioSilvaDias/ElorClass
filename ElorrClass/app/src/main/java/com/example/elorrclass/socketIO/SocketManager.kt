package com.example.elorrclass.socketIO

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketManager {

    private lateinit var socket: Socket

    fun initSocket(){
        try {
            val options = IO.Options()
            options.reconnection = true
            socket = IO.socket("Aqui va la direccion", options)
            socket.connect()

            socket.on(Socket.EVENT_CONNECT) {
                println("Conectando al Servidor")
            }

            socket.on("custom_event") { args ->
                val data = args[0] as JSONObject
                println("Evento recibido: $data")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun emitEvent(eventName: String, data: JSONObject) {
        if (::socket.isInitialized && socket.connected()) {
            socket.emit(eventName, data)
            println("Evento emitido: $eventName, datos: $data")
        } else {
            println("Error: El socket no está inicializado o no está conectado.")
        }
    }

    fun disconnect() {
        if (::socket.isInitialized) {
            socket.disconnect()
            println("Servidor desconectado")
        }
    }

    fun isConnected(): Boolean {
        return ::socket.isInitialized && socket.connected()
    }
}