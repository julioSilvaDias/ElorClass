package com.example.elorrclass

import com.google.gson.*
import java.lang.reflect.Type
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class TimestampAdapter : JsonDeserializer<Timestamp>, JsonSerializer<Timestamp> {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH)

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Timestamp {
        return try {
            val dateString = json?.asString
                ?.replace(Regex("[\\u202F\\u00A0]"), " ")
                ?.replace(", ", ", ")

            val date = dateFormat.parse(dateString)
            Timestamp(date.time)
        } catch (e: Exception) {
            throw JsonParseException("Error al analizar la fecha: ${json?.asString}", e)
        }
    }

    override fun serialize(src: Timestamp?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateFormat.format(src))
    }
}


