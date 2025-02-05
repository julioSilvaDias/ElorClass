package com.example.elorrclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elorrclass.adapter.CursoExternosAdapter
import com.example.elorrclass.adapter.TimestampAdapter
import com.example.elorrclass.pojos.CursosExternos
import com.example.elorrclass.socketIO.SocketManager

class MainActivityCursosExternos : AppCompatActivity() {
    private val socketManager = SocketManager(this)
    private lateinit var adapter: CursoExternosAdapter
    private var selectedCurso: CursosExternos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_cursos_externos)

        adapter = CursoExternosAdapter(this, ArrayList())
        val listView = findViewById<ListView>(R.id.listView2)
        listView.adapter = adapter

        val btnVerCurso = findViewById<Button>(R.id.button2)
        btnVerCurso.isEnabled = false

        listView.setOnItemClickListener { parent, view, position, id ->
            selectedCurso = adapter.getItem(position)
            btnVerCurso.isEnabled= true
        }

        btnVerCurso.setOnClickListener{
            selectedCurso?.let { curso->
                val intent = Intent(this, MainActivityVerCurso::class.java)
                intent.putExtra("selectedCurso", curso)
            }
        }

        socketManager.connect()
        socketManager.getAllCursos()
    }

    fun handleCursosExternosResponse(cursosExternos: List<CursosExternos>){
        runOnUiThread{
            (adapter as CursoExternosAdapter).apply {
                clear()
                addAll(cursosExternos)
                notifyDataSetChanged()
            }
        }
    }
}