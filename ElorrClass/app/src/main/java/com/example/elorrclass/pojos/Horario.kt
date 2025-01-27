package com.example.elorrclass.pojos

import java.io.Serializable

data class Horario(
    var id: Int? = null,
    var asignatura: Asignatura? = null,
    var usuario: Usuario? = null,
    var dia: String? = null,
    var tipo: String? = null,
    var aula: String? = null
) : Serializable
