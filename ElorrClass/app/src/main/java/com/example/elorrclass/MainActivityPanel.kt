package com.example.elorrclass

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.gridlayout.widget.GridLayout
import com.example.elorrclass.pojos.Horario
import com.example.elorrclass.pojos.Usuario
import com.example.elorrclass.socketIO.SocketManager

class MainActivityPanel : AppCompatActivity() {
    private val socketManager = SocketManager(this)
    private lateinit var gridLayoutHorario: GridLayout
    private lateinit var spinnerSemana: Spinner

    private var horarios: List<Horario> = emptyList()
    private var semanaSeleccionada = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_panel)

        gridLayoutHorario = findViewById(R.id.gridLayoutHorario)
        spinnerSemana = findViewById(R.id.spinnerSemana)

        socketManager.connect()

        val username = intent.getStringExtra("username")
        if(username != null){
            socketManager.getUserId(username)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.semanas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSemana.adapter = adapter
        }

        spinnerSemana.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long ){
                semanaSeleccionada = position +1
                actualizarTabla()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun handleUserResponse(usuario: Usuario) {
        socketManager.getHorario(usuario.id)
    }

    fun handleHorarioResponse(nuevosHorarios: List<Horario>) {
        print(horarios)
        runOnUiThread {
            horarios = nuevosHorarios
            actualizarTabla()
        }
    }

    private fun actualizarTabla(){
        gridLayoutHorario.removeAllViews()

        val filas = 6
        val columnas = 5

        val anchoPantalla = resources.displayMetrics.widthPixels
        val anchoCelda = anchoPantalla / columnas

        val matriz = Array(filas){ arrayOfNulls<String>(columnas) }

        for(horario in horarios){
            if(horario.semana == semanaSeleccionada){
                val fila = horario.hora?.toInt()?.minus(1) ?:0
                val columna = when (horario.dia){
                    "Lunes" -> 0
                    "Martes" -> 1
                    "Miércoles" -> 2
                    "Jueves" -> 3
                    "Viernes" -> 4

                    else -> continue
                }

                matriz[fila][columna] = horario.asignatura?: "Sin asignatura"
            }
        }

        val alturasFilas = IntArray(filas){0}
        val textViews = Array(filas) {arrayOfNulls<TextView>(columnas)}

        for(i in 0 until 6){
            for(j in 0 until 5){
                val textView = TextView(this)
                textView.text = matriz[i][j]?:""
                textView.textSize = 14f
                textView.gravity = Gravity.CENTER
                textView.setPadding(8)
                textView.background = getDrawable(R.drawable.cell_border)

                textView.setSingleLine(false)
                textView.maxLines = Integer.MAX_VALUE

                textView.minWidth = anchoCelda
                textView.layoutParams = GridLayout.LayoutParams().apply {
                    width = anchoCelda
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(4,4,4,4)
                }

                textViews[i][j]=textView
                gridLayoutHorario.addView(textView)
                textView.measure(0,0)
                alturasFilas[i] = maxOf(alturasFilas[i], textView.measuredHeight)
            }
        }

        for(i in 0 until filas){
            for(j in 0 until  columnas){
                textViews[i][j]?.height = alturasFilas[i]
            }
        }
    }
}

