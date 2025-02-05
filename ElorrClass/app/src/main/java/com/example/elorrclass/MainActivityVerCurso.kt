package com.example.elorrclass

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elorrclass.pojos.CursosExternos
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class MainActivityVerCurso : AppCompatActivity() {

    private lateinit var selectedCurso: CursosExternos
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ver_curso)

        selectedCurso = intent.getSerializableExtra("selectedCurso") as CursosExternos

        val tvTitulo = findViewById<TextView>(R.id.tvTituloDetail)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionDetail)
        val tvFechaInicio = findViewById<TextView>(R.id.tvFechaInicioDetail)
        val tvHorario = findViewById<TextView>(R.id.tvHorarioDetail)
        val tvContacto = findViewById<TextView>(R.id.tvHorarioDetail)

        tvTitulo.text = selectedCurso.titulo?:""
        tvDescripcion.text = selectedCurso.descripcion?:""
        tvFechaInicio.text = "Inicio: " + (selectedCurso.fechaInicio?.toString()?:"N/A")
        tvHorario.text = "Horario: " + (selectedCurso.horario?:"")
        tvContacto.text = "Contacto: " + (selectedCurso.contacto?:"")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
}