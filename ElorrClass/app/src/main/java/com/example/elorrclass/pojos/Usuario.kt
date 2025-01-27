package com.example.elorrclass.pojos

import java.io.Serializable

data class Usuario(
    var id: Int? = null,
    var ciclo: Ciclo? = null,
    var login: String? = null,
    var password: String? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var email: String? = null,
    var telefono: String? = null,
    var foto: ByteArray? = null,
    var tipoUsuario: String? = null,
    var cicloFormativo: String? = null,
    var curso: Int? = null,
    var duales: Int? = null,
    var dni: String? = null,
    var telefono1: String? = null,
    var telefono2: String? = null,
    var alumno: Alumno? = null,
    var citasForIdProfesor: Set<Any> = emptySet(),
    var horarios: Set<Any> = emptySet(),
    var profesor: Profesor? = null,
    var citasForIdAlumno: Set<Any> = emptySet()
) : Serializable
