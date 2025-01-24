package com.example.elorrclass.pojos

import java.io.Serializable
import java.sql.Date

data class CursosExternos(
    var id: Int? = null,
    var titulo: String? = null,
    var descripcion: String? = null,
    var fechaInicio: Date? = null,
    var fechaFin: Date? = null,
    var horario: String? = null,
    var lugar: String? = null,
    var contacto: String? = null,
    var cicloFormativo: String? = null,
    var curso: Int? = null,
    var asignatura: String? = null
) : Serializable
