package com.example.elorrclass

import android.content.Context
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import bbdd.pojos.Reunion
import java.text.SimpleDateFormat
import java.util.Locale

class ReunionAdapter(
    context: Context,
    private val reuniones: MutableList<Reunion>
) : ArrayAdapter<Reunion>(context, 0, reuniones) {

    fun clearItems() {
        reuniones.clear()
        notifyDataSetChanged()
    }

    fun addItems(newReuniones: List<Reunion>) {
        reuniones.addAll(newReuniones)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val reunion = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_reunion, parent, false)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val tvFechaHora = view.findViewById<TextView>(R.id.tvFechaHora)
        val tvParticipantes = view.findViewById<TextView>(R.id.tvParticipantes)
        val tvEstado = view.findViewById<TextView>(R.id.tvEstado)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaIncio = dateFormat.format(reunion?.fechaHoraInicio)

        val participantes = reunion?.citas?.flatMap { cita ->
            listOf(
                cita.usuarioPropietario?.nombre,
                cita.usuarioDestinatario?.nombre
            )
        }?.filterNotNull()?.distinct()?.joinToString { ", " } ?: "Sin participantes"

        tvTitulo.text = reunion?.titulo ?: "Sin titulo"
        tvFechaHora.text = "Fecha: $fechaIncio"
        tvParticipantes.text = "Participantes: $participantes"
        tvEstado.text = reunion?.estado?: " "


        return view
    }

}