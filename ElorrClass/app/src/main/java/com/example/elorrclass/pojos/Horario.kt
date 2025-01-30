package com.example.elorrclass.pojos

import java.io.Serializable

data class Horario(
    var id: Int? = null,
    var asignatura: String? = null,
    var id_Usuario: Int = 0,
    var dia: String? = null,
    var tipo: String? = null,
    var aula: String? = null
) : Serializable
