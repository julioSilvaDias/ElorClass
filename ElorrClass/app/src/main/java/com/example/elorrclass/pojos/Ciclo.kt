package com.example.elorrclass.pojos

import java.io.Serializable

data class Ciclo(
    var id: Int,
    var codigo: Int? = null,
    var nombre: String? = null,
    var descripcion: String? = null,
    var asignaturas: Set<Any> = emptySet(),
    var usuarios: Set<Any> = emptySet()
) : Serializable