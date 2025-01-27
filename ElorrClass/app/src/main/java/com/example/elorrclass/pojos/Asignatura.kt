package com.example.elorrclass.pojos
import java.io.Serializable

data class Asignatura(
    var id: Int = 0,
    var nombre: String? = null,
    var curso: Int? = null,
    var horarios: MutableSet<Any> = mutableSetOf(),
    var ciclos: MutableSet<Any> = mutableSetOf()
) : Serializable