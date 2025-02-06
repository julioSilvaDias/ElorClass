package com.example.elorrclass

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.elorrclass.pojos.CursosExternos
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivityVerCurso : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var selectedCurso: CursosExternos
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ver_curso)

        selectedCurso = intent.getSerializableExtra("selectedCurso") as CursosExternos

        val btn_volver = findViewById<Button>(R.id.btnVolverCurso)
        btn_volver.setOnClickListener {
            finish()
        }

        val tvTitulo = findViewById<TextView>(R.id.tvTituloDetail)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionDetail)
        val tvFechaInicio = findViewById<TextView>(R.id.tvFechaInicioDetail)
        val tvHorario = findViewById<TextView>(R.id.tvHorarioDetail)
        val tvContacto = findViewById<TextView>(R.id.tvContactoDetail)

        tvTitulo.text = selectedCurso.titulo ?: ""
        tvDescripcion.text = selectedCurso.descripcion ?: ""
        tvFechaInicio.text = "Inicio: " + (selectedCurso.fechaInicio?.toString() ?: "N/A")
        tvHorario.text = "Horario: " + (selectedCurso.horario ?: "")
        tvContacto.text = "Contacto: " + (selectedCurso.contacto ?: "")

        Log.d("MainActivityVerCurso", "Lugar recibido: ${selectedCurso.lugar}")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val coords = selectedCurso.lugar?.split(",")?.map { it.trim().toDoubleOrNull() }
        Log.d("MainActivityVerCurso", "Coordenadas obtenidas: $coords")
        if (coords != null && coords.size == 2 && coords[0] != null && coords[1] != null) {
            val latLng = LatLng(coords[0]!!, coords[1]!!)
            Log.d("MainActivityVerCurso", "LatLng: $latLng")
            map.addMarker(MarkerOptions().position(latLng).title(selectedCurso.titulo))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
            Log.e("MainActivityVerCurso", "Error al parsear coordenadas")
        }
    }
}
