package bbdd.pojos

import com.example.elorrclass.pojos.Cita
import java.io.Serializable
import java.sql.Timestamp

data class Reunion(
    var id: Int = 0,
    var fechaHoraInicio: Timestamp? = null,
    var fechaHoraFin: Timestamp? = null,
    var titulo: String? = null,
    var estado: String? = null,
    var comentario: String? = null,
    var numAceptadas: Int = 0,
    var aula: String? = null,
    var citas: Set<Cita> = emptySet()
) : Serializable
