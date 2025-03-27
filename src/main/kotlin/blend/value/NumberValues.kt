package blend.value

import com.google.gson.JsonObject
import kotlin.math.roundToInt

class IntValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Int,
    minimum: Int,
    maximum: Int,
    incrementBy: Int = 1
): AbstractNumberValue<Int>(name, parent, visibility, defaultValue, minimum, maximum, incrementBy) {
    override fun set(newValue: Int) {
        super.set(newValue.coerceIn(minimum, maximum))
    }
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asInt)
            true
        } catch (_: Exception) {
            false
        }
    }
}

class FloatValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Float,
    minimum: Float,
    maximum: Float,
    incrementBy: Float
): AbstractNumberValue<Float>(name, parent, visibility, defaultValue, minimum, maximum, incrementBy) {
    override fun set(newValue: Float) {
        val steps = ((newValue.coerceIn(minimum, maximum) - minimum) / incrementBy).roundToInt()
        super.set(minimum + steps * incrementBy)
    }
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asFloat)
            true
        } catch (_: Exception) {
            false
        }
    }
}

class DoubleValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Double,
    minimum: Double,
    maximum: Double,
    incrementBy: Double
): AbstractNumberValue<Double>(name, parent, visibility, defaultValue, minimum, maximum, incrementBy) {
    override fun set(newValue: Double) {
        val steps = ((newValue.coerceIn(minimum, maximum) - minimum) / incrementBy).roundToInt()
        super.set(minimum + steps * incrementBy)
    }
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asDouble)
            true
        } catch (_: Exception) {
            false
        }
    }
}
