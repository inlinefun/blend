// VERY CHAOTIC FILE
package blend.value

import blend.module.AbstractModule
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ValueParent {
    val values: MutableList<AbstractValue<*>>
}

abstract class AbstractValue<T>(
    val name: String,
    val parent: ValueParent,
    val visibility: () -> Boolean,
    private val initialValue: T
) {
    internal var value = initialValue
    init {
        addToParent()
    }
    private fun addToParent() = parent.values.add(this) // separate function to prevent 'leaking this'
    open fun get() = value
    open fun set(updatedValue: T) {
        this.value = updatedValue
    }
    fun reset() = set(initialValue)
}

/**
 * Q: is this a bad idea?
 *
 * A: probably
 */
abstract class ReadWriteValue<T>(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: T
): AbstractValue<T>(name, parent, visibility, initialValue), ReadWriteProperty<AbstractModule, T> {
    override fun getValue(thisRef: AbstractModule, property: KProperty<*>) = get()
    override fun setValue(thisRef: AbstractModule, property: KProperty<*>, value: T) = set(value)
}

class ExpandableValue(
    name: String,
    parent: AbstractModule,
    visibility: () -> Boolean
): AbstractValue<Boolean>(name, parent, visibility, false), ValueParent, ReadOnlyProperty<AbstractModule, ExpandableValue> {
    override val values = mutableListOf<AbstractValue<*>>()
    override fun getValue(thisRef: AbstractModule, property: KProperty<*>): ExpandableValue = this
}

class KeybindingValue(
    name: String,
    parent: AbstractModule,
    visibility: () -> Boolean,
    key: Int,
    val hold: Boolean = false
): AbstractValue<Int>(name, parent, visibility, key), ReadOnlyProperty<AbstractModule, KeybindingValue> {
    override fun getValue(thisRef: AbstractModule, property: KProperty<*>): KeybindingValue = this
}

class BooleanValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: Boolean
): ReadWriteValue<Boolean>(name, parent, visibility, initialValue) {
    fun toggle() {
        set(!this.value)
    }
}

class ColorValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: Color,
    val hasAlpha: Boolean = false
): ReadWriteValue<Color>(name, parent, visibility, initialValue) {
    private var hue: Float = 0.0f
    private var saturation: Float = 1.0f
    private var brightness: Float = 1.0f
    private var alpha: Float = 1.0f

    init {
        set(initialValue)
    }

    override fun get(): Color {
        val baseColor = Color.getHSBColor(hue, saturation, brightness)
        return Color(
            baseColor.red, baseColor.green, baseColor.blue,
            if (hasAlpha) (alpha * 255).toInt() else 255
        )
    }

    override fun set(updatedValue: Color) {
        val hsb = Color.RGBtoHSB(updatedValue.red, updatedValue.green, updatedValue.blue, null)
        set(hsb[0], hsb[1], hsb[2])
        this.alpha = (updatedValue.alpha / 255.0f).coerceIn(0.0f, 1.0f)
    }
    fun set(hue: Float, saturation: Float, brightness: Float) {
        this.hue = hue.coerceIn(0.0f, 1.0f)
        this.saturation = saturation.coerceIn(0.0f, 1.0f)
        this.brightness = brightness.coerceIn(0.0f, 1.0f)
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
            "Empty array provided in $name of property ${this::class.simpleName}"
        }
    }

    override fun set(updatedValue: String) {
        if (availableOptions.contains(updatedValue))
            super.set(updatedValue)
    }
}

abstract class AbstractNumberValue<T: Number>(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: T,
    val minValue: T,
    val maxValue: T,
    val increment: T
): ReadWriteValue<T>(name, parent, visibility, initialValue) {
    init {
        require(minValue.toDouble() < maxValue.toDouble()) {
            "Min value is lesser than Max value in $name of parent ${parent::class.simpleName}"
        }
        require(increment.toDouble() > 0) {
            "Incremental value is lesser than 0 in $name of parent ${parent::class.simpleName}"
        }
        require((maxValue.toDouble() - minValue.toDouble()) % increment.toDouble() == 0.0) {
            "Increment value ${increment.toDouble()} of $name in parent ${parent::class.simpleName} must fit evenly into range $minValue - $maxValue"
        }
    }
    abstract override fun set(updatedValue: T)
}

class IntegerValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: Int,
    minValue: Int,
    maxValue: Int
): AbstractNumberValue<Int>(name, parent, visibility, initialValue, minValue, maxValue, 1) {
    override fun set(updatedValue: Int) {
        this.value = updatedValue.coerceIn(minValue, maxValue)
    }
}

class FloatValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: Float,
    minValue: Float,
    maxValue: Float,
    increment: Float
): AbstractNumberValue<Float>(name, parent, visibility, initialValue, minValue, maxValue, increment) {
    override fun set(updatedValue: Float) {
        val steps = ((updatedValue.coerceIn(minValue, maxValue) - minValue) / increment).roundToInt()
        this.value = minValue + steps * increment
    }
}

class DoubleValue(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    initialValue: Double,
    minValue: Double,
    maxValue: Double,
    increment: Double
): AbstractNumberValue<Double>(name, parent, visibility, initialValue, minValue, maxValue, increment) {
    override fun set(updatedValue: Double) {
        val steps = ((updatedValue.coerceIn(minValue, maxValue) - minValue) / increment).roundToInt()
        this.value = minValue + steps * increment
    }
}