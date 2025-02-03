package com.example.elorrclass.pojos
import bbdd.pojos.Reunion
import java.io.Serializable

data class Cita(
    var id: CitaId? = null,
    var reunion: Reunion? = null,
    var usuarioPropietario: Usuario? = null,
    var usuarioDestinatario: Usuario? = null
) : Serializable