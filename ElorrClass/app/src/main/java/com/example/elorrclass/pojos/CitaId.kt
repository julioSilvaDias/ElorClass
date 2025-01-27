package com.example.elorrclass.pojos

import java.io.Serializable

data class CitaId(
    var idAlumno: Int = 0,
    var idProfesor: Int = 0,
    var idReunion: Int = 0
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as CitaId
        return idAlumno == other.idAlumno &&
                idProfesor == other.idProfesor &&
                idReunion == other.idReunion
    }

    override fun hashCode(): Int {
        var result = 17
        result = 37 * result + idAlumno
        result = 37 * result + idProfesor
        result = 37 * result + idReunion
        return result
    }
}
