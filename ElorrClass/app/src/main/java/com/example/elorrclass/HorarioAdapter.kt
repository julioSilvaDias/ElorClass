package com.example.elorrclass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elorrclass.R
import com.example.elorrclass.pojos.Horario

class HorarioAdapter(private var horarios: List<Horario>) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.diaTextView.text = horario.dia
        holder.asignaturaTextView.text = horario.asignatura
        holder.aulaTextView.text = horario.aula
    }

    override fun getItemCount(): Int {
        return horarios.size
    }

    fun updateHorarioList(newHorarios: List<Horario>) {
        this.horarios = newHorarios
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diaTextView: TextView = itemView.findViewById(R.id.textViewDia)
        val asignaturaTextView: TextView = itemView.findViewById(R.id.textViewAsignatura)
        val aulaTextView: TextView = itemView.findViewById(R.id.textViewAula)
    }
}


