package com.example.elorrclass.pojos

import java.io.Serializable

data class Profesor(
    var id: Int = 0,
    var usuario: Usuario? = null
) : Serializable