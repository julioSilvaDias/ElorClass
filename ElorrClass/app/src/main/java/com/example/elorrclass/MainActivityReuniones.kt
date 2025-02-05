package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import bbdd.pojos.Reunion
import com.example.elorrclass.socketIO.SocketManager

class MainActivityReuniones : AppCompatActivity() {

    private val socketManager = SocketManager(this)
    private lateinit var adapter: ReunionAdapter
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_reuniones)

        adapter = ReunionAdapter(this, ArrayList())
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

        val username = intent.getStringExtra("username")
        val userId = intent.getIntExtra("userId", 0)
        socketManager.connect()
        socketManager.getReuniones(userId)

        findViewById<Button>(R.id.btn_volver).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityPanel::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button_crear).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityCrearReunion::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    fun handleReunionResponse(reuniones: List<Reunion>) {
        runOnUiThread {
            (adapter as ReunionAdapter).apply {
                clear()
                addAll(reuniones)
                notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}