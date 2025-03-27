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
    init {
        require(minimum < maximum) {
            "Min value is lesser than Max value in $name of parent ${parent::class.simpleName}"
        }
        require(incrementBy > 0) {
            "Incremental value is lesser than 0 in $name of parent ${parent::class.simpleName}"
        }
        require((maximum - minimum.toDouble()) % incrementBy == 0.0) {
            "Increment value $incrementBy of $name in parent ${parent::class.simpleName} must fit evenly into range $minimum - $maximum"
        }
    }
    override fun set(newValue: Int) {
        val steps = (newValue.coerceIn(minimum, maximum) - minimum) / incrementBy
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
            set(obj.get("value").asInt)
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
    init {
        require(minimum < maximum) {
            "Min value is lesser than Max value in $name of parent ${parent::class.simpleName}"
        }
        require(incrementBy > 0) {
            "Incremental value is lesser than 0 in $name of parent ${parent::class.simpleName}"
        }
        val scaleFactor = 1e9 // Choose a large enough power of ten
        val scaledMin = (minimum * scaleFactor).toLong()
        val scaledMax = (maximum * scaleFactor).toLong()
        val scaledIncrement = (incrementBy * scaleFactor).toLong()
        require((scaledMax - scaledMin) % scaledIncrement == 0L) {
            "Increment value $incrementBy of $name in parent ${parent::class.simpleName} must fit evenly into range $minimum - $maximum"
        }
    }
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
