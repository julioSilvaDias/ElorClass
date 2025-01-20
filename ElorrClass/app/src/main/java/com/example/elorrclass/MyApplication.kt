package com.example.elorrclass

import android.app.Application
import com.example.elorrclass.socketIO.SocketManager

class MyApplication : Application(){
    lateinit var socketManager: SocketManager

    override fun onCreate() {
        super.onCreate()
        socketManager = SocketManager(this)
        socketManager.connect()
    }
}