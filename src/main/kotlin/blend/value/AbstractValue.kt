package blend.value

import blend.module.AbstractModule
import blend.util.extensions.addChatWarn
import com.google.gson.JsonObject
import net.minecraft.text.Text
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ValueParent {
    val values: MutableList<AbstractValue<*>>
}

abstract class AbstractValue<T>(
    val name: String,
    val parent: ValueParent,
    val visibility: () -> Boolean,
    private val defaultValue: T
) {
    private var value = defaultValue
    init {
        addToParent()
    }
    private fun addToParent() = parent.values.add(this)
    protected fun loadError() = Text.of("Failed to load $name in ${if (parent is AbstractModule) parent.name else parent::class.simpleName}").addChatWarn()
    open fun get() = value
    open fun set(newValue: T) {
        this.value = newValue
    }
    abstract fun getJsonObject(): JsonObject
    abstract fun useJsonObject(obj: JsonObject): Boolean
}

abstract class ReadWriteValue<T>(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: T
): AbstractValue<T>(
    name, parent, visibility, defaultValue
), ReadWriteProperty<AbstractModule, T> {
    override fun getValue(thisRef: AbstractModule, property: KProperty<*>): T = get()
    override fun setValue(thisRef: AbstractModule, property: KProperty<*>, value: T) = set(value)
}

abstract class AbstractNumberValue<T: Number>(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: T,
    val minimum: T,
    val maximum: T,
    val incrementBy: T
): ReadWriteValue<T>(name, parent, visibility, defaultValue) {
    init {
        require(minimum.toDouble() < maximum.toDouble()) {
            "Min value is lesser than Max value in $name of parent ${parent::class.simpleName}"
        }
        require(incrementBy.toDouble() > 0) {
            "Incremental value is lesser than 0 in $name of parent ${parent::class.simpleName}"
        }
        require((maximum.toDouble() - minimum.toDouble()) % incrementBy.toDouble() == 0.0) {
            "Increment value ${incrementBy.toDouble()} of $name in parent ${parent::class.simpleName} must fit evenly into range $minimum - $maximum"
        }
    }
}

abstract class AbstractRangeValue<T: Number>(
    name: String,
    parent: ValueParent,
    visibility: () -> Boolean,
    defaultValue: Pair<T, T>,
    val minimum: T,
    val maximum: T,
    val incrementBy: T
): ReadWriteValue<Pair<T, T>>(
    name, parent, visibility, defaultValue
) {
    init {
        require(minimum.toDouble() < maximum.toDouble()) {
            "Min value is lesser than Max value in $name of parent ${parent::class.simpleName}"
        }
        require(incrementBy.toDouble() > 0) {
            "Incremental value is lesser than 0 in $name of parent ${parent::class.simpleName}"
        }
        require((maximum.toDouble() - minimum.toDouble()) % incrementBy.toDouble() == 0.0) {
            "Increment value ${incrementBy.toDouble()} of $name in parent ${parent::class.simpleName} must fit evenly into range $minimum - $maximum"
        }
    }
}
