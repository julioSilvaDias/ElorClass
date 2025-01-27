package com.example.elorrclass.pojos

import java.io.Serializable

data class Alumno(
    var id: Int = 0,
    var usuario: Usuario? = null
) : Serializable