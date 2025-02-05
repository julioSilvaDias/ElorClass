import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class CustomSqlDateAdapter : TypeAdapter<Date>() {
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale("es", "ES"))

    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(dateFormat.format(value))
    }

    override fun read(reader: JsonReader): Date? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        val dateStr = reader.nextString()
        return try {
            Date(dateFormat.parse(dateStr).time)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }
}