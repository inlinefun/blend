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
    defaultKey: Int = GLFW.GLFW_KEY_UNKNOWN
): Subscriber, IAccessor, ValueParent {

    override val values = mutableListOf<AbstractValue<*>>()

    private var enabled = false
    protected open val canBeEnabled = true
    val bind by bind("Bind", defaultKey)
    val name = names.first()

    open fun onEnable() {}
    open fun onDisable() {}

    fun get() = enabled
    fun set(enabled: Boolean) {
        if (this.enabled != enabled) {
            this.enabled = enabled
            if (this.enabled) {
                onEnable()
                EventBus.register(this)
            } else {
                EventBus.unregister(this)
                onDisable()
            }
        }
    }
    fun toggle() {
        set(!enabled)
    }

    // all values
    fun boolean(name: String, value: Boolean, parent: ValueParent = this, visibility: () -> Boolean = { true }) = BooleanValue(name, parent, visibility, value)
    fun int(name: String, value: Int, min: Int, max: Int, parent: ValueParent = this, visibility: () -> Boolean = { true }) = IntegerValue(name, parent, visibility, value, min, max)
    fun float(name: String, value: Float, min: Float, max: Float, increment: Float, parent: ValueParent = this, visibility: () -> Boolean = { true }) = FloatValue(name, parent, visibility, value, min, max, increment)
    fun double(name: String, value: Double, min: Double, max: Double, increment: Double, parent: ValueParent = this, visibility: () -> Boolean = { true }) = DoubleValue(name, parent, visibility, value, min, max, increment)
    fun list(name: String, values: Array<String>, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ListValue(name, parent, visibility, values)
    fun color(name: String, value: Color, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ColorValue(name, parent, visibility, value)
    fun parent(name: String, visibility: () -> Boolean = { true }) = ExpandableValue(name, this, visibility)
    fun bind(name: String, defaultKey: Int, parent: AbstractModule = this, visibility: () -> Boolean = { true }) = KeybindingValue(name, parent, visibility, defaultKey)
}

enum class ModuleCategory {
    COMBAT,
    MOVEMENT,
    PLAYER,
    VISUAL,
    OTHER;
    val formattedName = name.lowercase().replaceFirstChar { it.uppercase() }
}