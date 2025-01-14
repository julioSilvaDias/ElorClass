package com.example.elorrclass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.socketIO.SocketManager
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val socketManager = SocketManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        socketManager.initSocket()

        val testData = JSONObject()
        testData.put("message", "Comunicacion desde Android")
        socketManager.emitEvent("test_event", testData)

    }

    override fun onDestroy() {
        super.onDestroy()

        socketManager.disconnect()
    }
}