package blend.module

import blend.event.EventBus
import blend.event.Subscriber
import blend.util.interfaces.IAccessor
import blend.value.*
import org.lwjgl.glfw.GLFW
import java.awt.Color

abstract class AbstractModule(
    val names: Array<String>,
    val description: String,
    val category: ModuleCategory,
    val defaultEnabled: Boolean = false,
    defaultKey: Int = GLFW.GLFW_KEY_UNKNOWN,
    defaultHoldOnly: Boolean = false,
    val canBeEnabled: Boolean = true
): IAccessor, ValueParent, Subscriber {

    override val values: MutableList<AbstractValue<*>> = mutableListOf()
    val bind by bind("Bind", defaultKey, defaultHoldOnly)
    private var state = false
    val name = names.first()

    open fun onEnable() {}
    open fun onDisable() {}

    fun toggle() = set(!state)
    fun get() = state
    fun set(state: Boolean) {
        if (canBeEnabled && this.state != state) {
            this.state = state
            if (this.state) {
                onEnable()
                EventBus.register(this)
            } else {
                EventBus.unregister(this)
                onDisable()
            }
        }
    }

    fun boolean(name: String, value: Boolean, parent: ValueParent = this, visibility: () -> Boolean = { true }) = BooleanValue(name, parent, visibility, value)
    fun int(name: String, defaultValue: Int, minimum: Int, maximum: Int, incrementBy: Int = 1, parent: ValueParent = this, visibility: () -> Boolean = { true }) = IntValue(name, parent, visibility, defaultValue, minimum, maximum, incrementBy)
    fun double(name: String, value: Double, min: Double, max: Double, incrementBy: Double, parent: ValueParent = this, visibility: () -> Boolean = { true }) = DoubleValue(name, parent, visibility, value, min, max, incrementBy)
    fun list(name: String, values: Array<String>, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ListValue(name, parent, visibility, values)
    fun color(name: String, value: Color, hasAlpha: Boolean = false, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ColorValue(name, parent, visibility, value, hasAlpha)
    fun bind(name: String, defaultKey: Int, hold: Boolean, parent: ValueParent = this, visibility: () -> Boolean = { true }) = KeyValue(name, parent, visibility, defaultKey, hold)
    fun parent(name: String, visibility: () -> Boolean = { true }) = ExpandableValue(name, this, visibility)
}

enum class ModuleCategory {
    COMBAT,
    MOVEMENT,
    PLAYER,
    VISUAL,
    OTHER;
    val formattedName = name.lowercase().replaceFirstChar { it.uppercase() }
}