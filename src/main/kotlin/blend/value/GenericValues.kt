package blend.value

import blend.module.AbstractModule
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ExpandableValue(
    name: String,
    parent: AbstractModule,
    visibility: () -> Boolean
): AbstractValue<Boolean>(name, parent, visibility, false), ValueParent {

    override val values: MutableList<AbstractValue<*>> = mutableListOf()

    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        val subValues = JsonArray()
        this.values.forEach { value ->
            subValues.add(value.getJsonObject())
        }
        obj.addProperty("name", name)
        obj.add("values", subValues)
        return obj
    }

    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            obj.get("values").asJsonArray.mapNotNull {
                it.asJsonObject
            }.forEach {
                this.values.firstOrNull {
                    it.name.equals("name", true)
                }?.useJsonObject(it)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

}

class BooleanValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Boolean
): ReadWriteValue<Boolean>(name, parent, visibility, defaultValue) {
    fun toggle() = set(!get())

    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asBoolean)
            true
        } catch (_: Exception) {
            loadError()
            false
        }
    }
}

class StringValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: String
): ReadWriteValue<String>(name, parent, visibility, defaultValue) {
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asString)
            true
        } catch (_: Exception) {
            loadError()
            false
        }
    }
}

class ColorValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Color,
    val translucent: Boolean
): ReadWriteValue<Color>(name, parent, visibility, defaultValue) {

    init {
        set(defaultValue)
    }

    var hue = 0.0f
    var saturation = 1.0f
    var brightness = 1.0f
    var alpha = 1.0f

    override fun get(): Color {
        val hmm = Color.getHSBColor(hue, saturation, brightness)
        val alpha = 255.times(if (translucent) this.alpha else 1.0f).roundToInt().coerceIn(0, 255)
        return Color(hmm.red, hmm.green, hmm.blue, alpha)
    }

    override fun set(newValue: Color) {
        set(Color.RGBtoHSB(newValue.red, newValue.green, newValue.blue, null))
    }
    fun set(hsb: FloatArray) {
        require(hsb.size == 3) {
            throw IllegalArgumentException("FloatArray size MUST be 3")
        }
        this.hue = hsb[0]
        this.saturation = hsb[1]
        this.brightness = hsb[2]
    }

    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("hue", hue)
        obj.addProperty("saturation", saturation)
        obj.addProperty("brightness", brightness)
        obj.addProperty("alpha", alpha)
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            val hsb = floatArrayOf(
                obj.get("hue").asFloat,
                obj.get("brightness").asFloat,
                obj.get("saturation").asFloat
            )
            set(hsb)
            obj.get("alpha")?.asFloat?.let {
                alpha = it
            }
            true
        } catch (_: Exception) {
            loadError()
            false
        }
    }
}

class ListValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    val availableOptions: Array<String>
): ReadWriteValue<String>(name, parent, visibility, availableOptions.first()) {

    init {
        require(availableOptions.isNotEmpty()) {
            throw IllegalArgumentException("Empty array provided in $name of parent ${parent::class.simpleName}")
        }
    }

    override fun set(newValue: String) {
        if (!availableOptions.contains(newValue))
            loadError()
        super.set(newValue)
    }
    fun next() {
        val index = availableOptions.indexOf(get())
        if (index < availableOptions.size - 1) {
            set(availableOptions[index + 1])
        } else {
            set(availableOptions.first())
        }
    }
    fun previous() {
        val index = availableOptions.indexOf(get())
        if (index > 0) {
            set(availableOptions[index - 1])
        } else {
            set(availableOptions.last())
        }
    }
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asString)
            true
        } catch (_: Exception) {
            loadError()
            false
        }
    }
}

class KeyValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultKey: Int,
    var hold: Boolean,
): AbstractValue<Int>(name, parent, visibility, defaultKey), ReadOnlyProperty<AbstractModule, KeyValue> {
    fun hold(value: Boolean) {
        this.hold = value
    }
    override fun getValue(thisRef: AbstractModule, property: KProperty<*>): KeyValue = this
    override fun getJsonObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("value", get())
        obj.addProperty("hold", hold)
        return obj
    }
    override fun useJsonObject(obj: JsonObject): Boolean {
        return try {
            set(obj.get("value").asInt)
            hold(obj.get("hold").asBoolean)
            true
        } catch (_: Exception) {
            loadError()
            false
        }
    }
}
