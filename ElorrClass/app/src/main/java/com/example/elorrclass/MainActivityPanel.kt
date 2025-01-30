package com.example.elorrclass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elorrclass.adapters.HorarioAdapter
import com.example.elorrclass.pojos.Horario
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.SocketManager

class MainActivityPanel : AppCompatActivity() {
    private val socketManager = SocketManager(this)
    private lateinit var recyclerViewHorario: RecyclerView
    private lateinit var adapter: HorarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_panel)
        socketManager.connect()

        recyclerViewHorario = findViewById(R.id.recyclerViewHorario)
        recyclerViewHorario.layoutManager = LinearLayoutManager(this)

        adapter = HorarioAdapter(emptyList())
        recyclerViewHorario.adapter = adapter

        val username = intent.getStringExtra("username")
        if (username != null) {
            socketManager.getUserId(username)
        }
    }

    fun handleUserResponse(usuario: Usuario) {
        socketManager.getHorario(usuario.id)
    }

    fun handleHorarioResponse(horarios: List<Horario>) {
        runOnUiThread {
            adapter.updateHorarioList(horarios)
        }
    }
}

