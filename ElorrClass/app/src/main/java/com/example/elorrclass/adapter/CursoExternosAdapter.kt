package com.example.elorrclass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.elorrclass.R
import com.example.elorrclass.pojos.CursosExternos
import java.text.SimpleDateFormat
import java.util.Locale

class CursoExternosAdapter(
    context: Context,
    private val cursosExternos: MutableList<CursosExternos>
): ArrayAdapter<CursosExternos>(context, 0, cursosExternos) {

    fun clearItems(){
        cursosExternos.clear()
        notifyDataSetChanged()
    }

    fun addIntems(newCursosExternos: List<CursosExternos>){
        cursosExternos.addAll(newCursosExternos)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cursoExterno = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_cursos_externos, parent, false)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo1)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val tvFechaIncio = view.findViewById<TextView>(R.id.tvFechaInicio1)
        val tvHorario = view.findViewById<TextView>(R.id.tvHorario)
        val tvContacto = view.findViewById<TextView>(R.id.tvContacto)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaInicio = dateFormat.format(cursoExterno?.fechaInicio)

        tvTitulo.text = cursoExterno?.titulo ?:""
        tvDescripcion.text = cursoExterno?.descripcion?: ""
        tvFechaIncio.text = "Fecha: $fechaInicio"
        tvHorario.text = cursoExterno?.horario ?: ""
        tvContacto.text= cursoExterno?.contacto?: ""

        return view
    }

}