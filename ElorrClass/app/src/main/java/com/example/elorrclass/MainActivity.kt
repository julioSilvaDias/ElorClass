package com.example.elorrclass

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.socketIO.SocketManager
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var socketManager : SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        socketManager = SocketManager()
        socketManager.initSocket()

        /*val videoView: VideoView = findViewById(R.id.videoView)
        val videoPath = "file:///android_asset/logoGift.mp4"

        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        videoView.start()*/
        
        if (socketManager.isConnected()) {
            val testData = JSONObject().apply {
                put("message", "Comunicacion desde Android")
            }
            socketManager.emitEvent("test_event", testData)
        } else {
            println("Socket no está conectado.")
        }

        val testData = JSONObject().apply {
            put("message", "Comunicación desde Android")
        }
        socketManager.emitEvent("test_event", testData)

    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}