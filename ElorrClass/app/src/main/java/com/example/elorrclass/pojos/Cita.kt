package com.example.elorrclass.pojos
import java.io.Serializable

data class Cita(
    var id: CitaId? = null,
    var reunion: Reunion? = null,
    var usuarioByIdProfesor: Usuario? = null,
    var usuarioByIdAlumno: Usuario? = null
) : Serializable